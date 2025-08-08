# MyTask – Android Task Management App (Demo Project)

This is a demo Android application built for educational and testing purposes. It provides simple task management features with user authentication and Firebase integration using MVVM architecture.

---

## 🔥 Features

- **User Registration & Login**
  - Implemented using **Firebase Authentication**
  - Validates correct email format and enforces password length (≥ 6 characters)
  - Displays success/error messages and loading indicators
- **Persistent Login State**
  - Handled using **SharedPreferences**
  - Users stay logged in until they manually log out
- **Task Management**
  - Create, view, and manage personal tasks
  - Empty fields are validated before submitting
- **Architecture**
  - Built using the **MVVM (Model-View-ViewModel)** design pattern
- **Clean UI**
  - Simple and user-friendly interface for better usability
- **Realtime Database**
  - Task data is stored and synced using **Firebase Cloud Firestore**

---

## 🧩 Firebase Integration

- This project uses **Firebase Authentication** for handling user login/registration securely.
- It uses **Cloud Firestore** to store user-specific task data in real time.
- On successful authentication, the app writes and reads tasks associated with the user's UID.
- All Firebase access is properly scoped via ViewModel to follow MVVM principles.

---

## 💾 Shared Preferences

- The login state is saved locally using `SharedPreferences`.
- When the app restarts, it checks login state to skip login screen if already authenticated.
- This keeps the user logged in across sessions unless they log out manually.

---

## ⚙️ How to Run

1. **Download** or **clone** the repository:
   ```bash
   git clone https://github.com/your-username/mytask.git
<img width="212" height="367" alt="image" src="https://github.com/user-attachments/assets/fde0a5ef-b012-4494-99cf-3f804415e7f0" />
<img width="315" height="608" alt="Screenshot 2025-08-06 200549" src="https://github.com/user-attachments/assets/21cef3fd-62a0-43a5-949a-cb2395daeee3" />
<img width="340" height="642" alt="Screenshot 2025-08-08 130540" src="https://github.com/user-attachments/assets/0b1efc83-7722-4fdf-bba4-1076b5706257" />
<img width="305" height="605" alt="Screenshot 2025-08-08 182131" src="https://github.com/user-attachments/assets/a8b1fee5-674e-4a3e-ae89-84b2e9ba50dd" />
<img width="326" height="626" alt="Screenshot 2025-08-08 140750" src="https://github.com/user-attachments/assets/753b1ffa-6810-4f0f-9d62-46ba5fca3d6d" />




