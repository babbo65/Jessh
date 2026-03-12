# Jessh MVP Roadmap & Gap Analysis

## Current State Summary

The project has a working MVC skeleton:

- **`model/Connection.java`** — POJO holding `username`, `password`, `ip`, `port`
- **`view/Add_connection.java`** — Swing form with username, password, IP fields and a Connect button
- **`controller/Connect.java`** — Opens a JSch `Session`, prints "Connected!", then immediately disconnects
- **`Main.java`** — Launches the GUI on the Swing EDT

The application compiles and can authenticate to an SSH server, but the session is torn down the moment it's established. There is no terminal panel, no command channel, and no output display.

---

## Gap Analysis

| Capability | Current State | MVP Requirement | Gap |
|---|---|---|---|
| Credential input UI | Done — username, password, IP fields | Same | None |
| SSH authentication | Done — JSch session connects successfully | Same | None |
| Keep session alive | Not done — `session.disconnect()` called immediately | Hold session open until user closes | Remove disconnect, manage session lifecycle |
| Open a shell channel | Not done | `ChannelShell` opened on the session | New logic in `Connect.java` |
| Display server output | Not done | Scrollable terminal output panel in the GUI | New `TerminalPanel` view component |
| Send commands | Not done | Text input field that writes to the channel's stdin | New input field wired to channel `OutputStream` |
| Connection error feedback | Not done — stack trace printed to console | Show error message in the UI | Try/catch with dialog or status label |
| Graceful disconnect | Not done | Button or window-close handler that closes channel + session cleanly | Session/channel teardown on close |

---

## Roadmap to MVP

### Step 1 — Refactor `Connect.java` to return a live session

**What:** Instead of connecting and disconnecting in one call, `openConnection` should return the active `Session` object (or throw on failure) so the caller can manage its lifetime.

**Why:** Everything else depends on having a session that stays open.

**Changes:**
- Change return type of `openConnection` from `void` to `Session`
- Remove `session.disconnect()` from the method
- Propagate `JSchException` up (or return `null` and let the view handle it)

---

### Step 2 — Add `ChannelShell` support to the controller

**What:** After the session is open, open a `ChannelShell` on it and expose the channel's `InputStream` (server output) and `OutputStream` (user input) to the caller.

**Why:** A `ChannelShell` gives a persistent interactive shell, which is what a terminal emulator needs. `ChannelExec` is for one-shot commands; `ChannelShell` is for interactive use.

**Changes:**
- New method (or extension of `openConnection`): opens `ChannelShell`, sets a `PipedInputStream`/`PipedOutputStream` pair, connects the channel, returns the streams
- Consider wrapping `Session` + `ChannelShell` + streams in a small value object (e.g., `ActiveConnection`) to pass around cleanly

---

### Step 3 — Build a `TerminalPanel` view component

**What:** A new Swing component that displays text output from the SSH server in a scrollable, read-only area.

**Why:** The current view has no output surface. This is the core visual element of the MVP.

**Changes:**
- New file: `view/TerminalPanel.java`
- Contains a `JTextArea` (non-editable) inside a `JScrollPane`
- Exposes an `appendText(String)` method for writing server output
- Auto-scrolls to bottom on new output

---

### Step 4 — Build a background reader thread

**What:** A `Thread` (or `SwingWorker`) that continuously reads from the channel's `InputStream` and pushes output to `TerminalPanel` via `SwingUtilities.invokeLater`.

**Why:** Reading from a blocking stream must happen off the EDT; updating Swing components must happen on it.

**Changes:**
- New class or inner class: `OutputReader` (implements `Runnable`)
- Reads bytes from the channel `InputStream` in a loop
- Calls `terminalPanel.appendText(...)` via `invokeLater`
- Stops when the channel is closed or an `IOException` is thrown

---

### Step 5 — Add command input to the view

**What:** A `JTextField` at the bottom of the main window where the user types commands. Pressing Enter sends the text (plus a newline) to the channel's `OutputStream`.

**Why:** Without input, the terminal is read-only and useless.

**Changes:**
- Add `JTextField commandInput` to `Add_connection` (or a new `TerminalView`)
- On Enter key: write `commandInput.getText() + "\n"` to channel `OutputStream`, flush, clear the field
- Wire this up only after a successful connection

---

### Step 6 — Replace the single-frame layout with a two-stage UI

**What:** After the user clicks Connect and the session opens, transition the UI from the credentials form to a terminal view (credentials panel hidden or replaced).

**Why:** The current single-frame layout has no room for a terminal panel, and showing credentials alongside an active session is poor UX.

**Options (pick one):**
- **Simple:** Hide the credentials panel with `setVisible(false)` and add `TerminalPanel` + command input to the same frame
- **Cleaner:** Open a new `JFrame` for the terminal and dispose the login frame

---

### Step 7 — Add connection error feedback

**What:** Show a visible error message in the UI when connection fails (wrong password, host unreachable, timeout, etc.).

**Why:** Currently errors silently print a stack trace to the console, which is invisible to an end user running the JAR.

**Changes:**
- Wrap `openConnection` call in the action listener with a try/catch
- On failure: show a `JOptionPane.showMessageDialog(...)` with the error message
- Common cases to handle: `JSchException` (auth failure, no route to host), `UnknownHostException`

---

### Step 8 — Implement graceful disconnect

**What:** When the user closes the terminal window (or clicks a Disconnect button), cleanly close the `ChannelShell` and then the `Session`.

**Why:** Leaving sessions open on the server wastes resources and can lock accounts.

**Changes:**
- Override `windowClosing` on the terminal frame (use `addWindowListener`)
- In the handler: stop the `OutputReader` thread, call `channel.disconnect()`, call `session.disconnect()`

---

## Suggested Implementation Order

```
Step 1  →  Step 2  →  Step 3  →  Step 4  →  Step 5  →  Step 6  →  Step 7  →  Step 8
Refactor   Shell      Terminal   Reader     Input      UI flow    Errors     Disconnect
Connect    channel    panel      thread     field      transition  dialogs    cleanup
```

Steps 1–2 are pure controller work with no UI changes. Steps 3–5 can be developed and tested in isolation before wiring everything together in Step 6. Steps 7–8 are polish that round out the MVP.

---

## Files to Create or Modify

| File | Action | Notes |
|---|---|---|
| `controller/Connect.java` | Modify | Return `Session`, add `ChannelShell` support |
| `view/Add_connection.java` | Modify | Two-stage layout, error dialogs, disconnect handler |
| `view/TerminalPanel.java` | Create | Scrollable output display |
| `controller/OutputReader.java` | Create | Background thread for reading channel output |
| `model/ActiveConnection.java` | Create (optional) | Wrapper for `Session` + `ChannelShell` + streams |

---

## Out of Scope for MVP

The following items from the original README are explicitly deferred:

- Secure credential storage
- SFTP support
- Multiple sessions / multithreading
- SSH key authentication
- Port forwarding
