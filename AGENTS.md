# SIM Switcher Project Rules & Design Guidelines

This project is a screenless, lightweight shortcut tool designed to launch the dual SIM settings screen directly from the Quick Settings panel in the notification area.

---

## 1. Core Design Principles (Dependency Minimization)

- **Do Not Use Jetpack Compose or UI Libraries**:
  - The app consists only of a transparent Activity and a TileService, without any UI. No UI libraries (such as Compose) are added.
- **Use Platform Activity**:
  - `MainActivity` inherits directly from the platform `android.app.Activity` instead of `androidx.activity.ComponentActivity` or other Jetpack libraries.
  - Maintains a lightweight build with zero runtime dependencies on `androidx` components inside the `dependencies` block.
- **Do Not Use Backward Compatibility Wrappers**:
  - Since the `minSdk` is `29` (Android 10) or higher, backward compatibility classes like `ContextCompat` are not used. Invoke native Context APIs (e.g., `checkSelfPermission`) directly.

---

## 2. State Monitoring & Update Rules (Real-Time Synchronization)

- **Use Official Listener APIs**:
  - To detect default data SIM changes in real time and update the tile, use `SubscriptionManager.OnSubscriptionsChangedListener`.
- **Lifecycle-Aware Listener Registration**:
  - Register the listener in `TileService.onStartListening()` and always unregister it in `onStopListening()` to completely prevent battery drain or memory load.

---

## 3. Quick Settings Tile State Management

- **Tile State Control Based on SIM Status**:
  - When the default data SIM is active ➔ Set the tile state to `Tile.STATE_ACTIVE` (on) and display the SIM name in the subtitle.
  - When the SIM is disabled or not inserted ➔ Set the tile state to `Tile.STATE_INACTIVE` (off) and display `SIM is disabled` in the subtitle.
  - When the SIM state cannot be retrieved (e.g., due to missing permissions) ➔ Set the tile state to `Tile.STATE_ACTIVE` (on) and display `Tap to open settings` in the subtitle.
