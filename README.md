# 🛍️ Swipe Android Assignment

This project is a fully functional Android app built using Jetpack Compose as part of the Swipe assignment.
It includes two main screens — a Product Listing screen and an Add Product screen — developed following modern Android development practices.

# 👁️ Architecture
<img width="1190" height="691" alt="image" src="https://github.com/user-attachments/assets/d3aeb662-9cb8-4a8a-b30a-c4007fb255eb" />


# 📸 Screenshots
| Product List Screen | Filtering | Searching | Adding |
|---------|---------|---------|---------|
| ![Image 1](https://github.com/user-attachments/assets/5e62d909-a0df-4faf-8bf8-2ff8883bfcbc) | ![Image 2](https://github.com/user-attachments/assets/68045f36-acd1-4829-93f0-9c591368d06b) | ![Image 3](https://github.com/user-attachments/assets/e18bf0b2-ffb4-4a5c-acac-3387de29d42a) | ![Image 4](https://github.com/user-attachments/assets/a526c43b-d7c0-40ec-bc57-47c121e98d03) |
| Add Product Screen | Input validation | Category Dialog | Adding Product (Offline) |
| ![Image 5](https://github.com/user-attachments/assets/0bfa992d-3ca6-4394-a424-fbb6335282f8) | ![Image 6](https://github.com/user-attachments/assets/4eac1252-8cd5-4c1a-ba9d-2d7c0511cd96) | ![Image 7](https://github.com/user-attachments/assets/5ce0412a-a7c4-4bed-a96a-46bbf798d35d) | ![Image 8](https://github.com/user-attachments/assets/e8fcddbc-6276-460f-bd44-e939ae569190) |
| Fetching Product Offline | Pending Upload | Pending Notification | Success Notification |
| ![Image 9](https://github.com/user-attachments/assets/14e69a7e-afdd-4acc-916d-104ea839cdbc) | ![Image 10](https://github.com/user-attachments/assets/2fc812d8-8ea2-4437-af20-0111951aae80) | ![Image 11](https://github.com/user-attachments/assets/32fc5bdf-0642-4552-b32f-1ef104f265c7) | ![Image 12](https://github.com/user-attachments/assets/1d2a57f2-9d84-4ffa-9e91-81b83b790fe6) |


# 🚀 Features Implemented
### 1 . Product Listing Screen
  - [x]  Displays products fetched from a public API
  - [x]  Supports search and filter functionality
  - [x] Loads images from URLs (with a default image fallback)
  - [x] Includes a progress indicator while data is loading
  - [x] Navigation to the Add Product screen

### 2 . Add Product Screen
  - [x]  Allows adding a new product with fields: product type, name, selling price, and tax rate
  - [x] Input validation for all fields
  - [x] Submits product details via POST API
  - [x] Displays feedback dialogs and upload progress
  - [x] Implements offline functionality — unsent products are stored locally and uploaded automatically once the device reconnects to the internet

# ⚙️ Tech Stack
- **Jetpack Compose** — Modern UI toolkit
- **MVVM Architecture** — Clean and maintainable structure
- **Retrofit** — REST API integration
- **Koin** — Dependency Injection
- **Async** — Coroutines + Flow
- **Local DB** — ROOM
- **Image Loading** — Coil
- **Navigation** — Jetpack Navigation Component
- **Ui State** — ViewModel + StateFlow
- **Serialization** — Gson/Kotlin Serialization
