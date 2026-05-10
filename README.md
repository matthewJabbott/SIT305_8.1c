# AI Chat App
 
## Project Features
 
* Username-based login screen to personalise the chat experience
* Real-time AI chat interface powered by Google Gemini
* Distinct message bubbles for user and AI responses
* Timestamps displayed on every message
* Local chat history persistence across sessions
* Flask backend proxying requests to the Gemini API
* Clean, modern UI using traditional Android XML layouts and RecyclerView
## Technology Stack
 
### Android (Client)
* Android (Kotlin)
* RecyclerView with dual ViewHolder types for user and AI messages
* Retrofit for HTTP communication with the backend
* Room Database for local chat history persistence
* Coroutines and Flow for asynchronous operations and reactive UI updates
### Backend (Server)
* Python with Flask
* Flask-CORS for cross-origin request handling
* Google Gemini API (`gemini-2.5-flash`) for AI response generation
## Architecture
 
* Multi-Activity pattern (login screen → chat screen)
* Client-server architecture with a Flask backend proxying Gemini API calls
* Room Database with Flow-based reactive queries for live message updates
* Coroutine-driven network and database calls on background threads
* RecyclerView adapter with view type switching for user vs AI message layouts
## Core Components
 
### Backend Layer (backend/app.py)
 
* **Flask server**: Exposes a `/chat` POST endpoint
* Accepts `username` and `message` in the request body
* Constructs a prompt and forwards it to the Gemini API
* Returns the AI response and a formatted timestamp
### Data Layer
 
* **ChatMessage**: Room entity storing message text, sender type (`USER` or `AI`), and timestamp
* **ChatDao**: DAO with a Flow query to stream all messages ordered by timestamp, and an insert for new messages
* **AppDatabase**: Singleton Room database instance managing the `chat_messages` table
### Networking
 
* **RetrofitInstance**: Singleton Retrofit client pointed at the local backend (`http://10.0.2.2:8080/`)
* **ChatApiService**: Retrofit interface defining the `POST /chat` endpoint with `ChatRequest` and `ChatResponse` data classes
### UI & State
 
* **MainActivity**: Login screen — captures a username and launches `ChatActivity` with it as an Intent extra
* **ChatActivity**: Main chat screen — observes the Room database via Flow, sends messages through Retrofit, and persists both user and AI messages locally
* **ChatAdapter**: RecyclerView adapter switching between `item_message_user` and `item_message_ai` layouts based on sender type
## Getting Started
 
1. Clone the repository: `git clone https://github.com/matthewJabbott/SIT305_8.1c.git`
### Starting the Backend
 
2. Navigate to the backend directory: `cd backend`
3. Install dependencies: `pip install flask flask-cors google-genai`
4. Run the server: `python app.py`
5. The server will start on `http://0.0.0.0:8080`
### Running the Android App
 
6. Open the project in Android Studio
7. The app is pre-configured to connect to `http://10.0.2.2:8080/` (the Android emulator's alias for localhost)
8. Ensure you have the required Android SDK installed
9. Build and run the app on an emulator or physical device
## Project Structure
 
```
├── app
│   ├── src
│   │   ├── main
│   │   │   ├── java
│   │   │   │   └── com.example.sit305_81c
│   │   │   │       ├── AppDatabase.kt
│   │   │   │       ├── ChatActivity.kt
│   │   │   │       ├── ChatAdapter.kt
│   │   │   │       ├── ChatApiService.kt
│   │   │   │       ├── ChatDao.kt
│   │   │   │       ├── ChatMessage.kt
│   │   │   │       ├── MainActivity.kt
│   │   │   │       └── RetrofitInstance.kt
│   │   │   ├── res
│   │   │   │   ├── layout
│   │   │   │   │   ├── activity_main.xml
│   │   │   │   │   ├── activity_chat.xml
│   │   │   │   │   ├── item_message_user.xml
│   │   │   │   │   └── item_message_ai.xml
│   │   │   │   └── values
│   │   │   │       ├── colors.xml
│   │   │   │       ├── strings.xml
│   │   │   │       └── themes.xml
│   │   │   └── AndroidManifest.xml
│   │   └── test
│   └── build.gradle.kts
├── backend
│   └── app.py
├── gradle
└── build.gradle.kts
```
 
## Key Implementation Details
 
### AI Integration
 
* The backend constructs a personalised prompt including the username before forwarding to Gemini, giving responses a more contextual feel
* The `gemini-2.5-flash` model is used for fast, concise replies
* Timestamps are generated server-side and also tracked client-side via `System.currentTimeMillis()`
### Chat Persistence
 
* Every message (user and AI) is immediately inserted into Room after being sent or received
* The `ChatDao` Flow query keeps the RecyclerView in sync automatically — no manual refresh needed
* Chat history is preserved across app restarts
### Performance & Responsiveness
 
* All database and network operations run inside `lifecycleScope.launch` coroutines, keeping the main thread free
* The RecyclerView scrolls to the latest message after each update
* Cleartext traffic is permitted in the manifest (`usesCleartextTraffic="true"`) to allow local HTTP connections to the backend during development
## Testing
 
* Unit tests located in `src/test/java/com/example/sit305_81c/`
* Instrumented tests located in `src/androidTest/java/com/example/sit305_81c/`
* Instructions for running tests:
  1. Open the project in Android Studio
  2. Ensure your emulator is running and the backend server is active
  3. Run tests from the Run menu or using Gradle commands
