# Simple file-transfer application

Server creates a fixed thread-pool of 10 connections.

---

### Supported commands

- list 
  - show all files in serverFiles/
- get fname
  - sends file from serverFiles/ to clientFiles/
- put fname 
  - sends file from clientFiles/ to serverFiles/
- bye 
  - closes connection


