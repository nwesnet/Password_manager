package nwes.passwordmanager;

public class SecurityManager {

    public static boolean isDoubleConfirmationEnabled() {
        return PreferencesManager.isDoubleConfirmationEnabled();
    }
    public static void setDoubleConfirmationEnabled(boolean enabled) {
        PreferencesManager.setDoubleConfirmation(enabled);
    }
    public static boolean validatePin(String inputPin) {
        String storePin = PreferencesManager.getPincode();
        return storePin != null && storePin.equals(inputPin);
    }

    public static void setStoreLogsEnabled(boolean enabled) {
        PreferencesManager.setStoreLogsEnabled(enabled);
        LogsManager.writeOptionalLog("Store logs option changed to " + enabled);
    }
    public static boolean isStoreLogsEnabled() {
        return PreferencesManager.isStoreLogsEnabled();
    }
}
