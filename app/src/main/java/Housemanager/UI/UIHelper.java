package Housemanager.UI;

public class UIHelper {
    public static Class<?> getEntityClass(String entityName) {
        // forName expects full package path
        entityName = "Housemanager.entities." + entityName;
        try {
            return Class.forName(entityName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
