package nwes.passwordmanager;

public class SecurityManager {
    private static boolean isTemporarilyDisabled = false;
    private static long tempDisableUntil = 0;

    public static boolean isDoubleConfirmationEnabled() {
        // Check if temporary override is active and valid
        if (isTemporarilyDisabled && System.currentTimeMillis() < tempDisableUntil) {
            return false; // Temporarily disabled
        }
        // Reset if expired
        if (System.currentTimeMillis() >= tempDisableUntil) {
            isTemporarilyDisabled = false;
            tempDisableUntil = 0;
        }

        return PreferencesManager.isDoubleConfirmationEnabled();
    }
    public static void setDoubleConfirmationEnabled(boolean enabled) {
        PreferencesManager.setDoubleConfirmation(enabled);
    }
    public static boolean validatePin(String inputPin) {
        String storePin = PreferencesManager.getPincode();
        return storePin != null && storePin.equals(inputPin);
    }
    public static void temporarilyDisableDoubleConfirmation(int minutes) {
        isTemporarilyDisabled = true;
        tempDisableUntil = System.currentTimeMillis() + (minutes * 60 * 1000L);
    }


    public static void setStoreLogsEnabled(boolean enabled) {
        PreferencesManager.setStoreLogsEnabled(enabled);
        LogsManager.writeOptionalLog("Store logs option changed to " + enabled);
    }
    public static boolean isStoreLogsEnabled() {
        return PreferencesManager.isStoreLogsEnabled();
    }
}
