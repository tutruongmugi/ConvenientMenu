package com.example.nguyenhuutu.convenientmenu;

import java.util.HashMap;
import java.util.Map;

public class Menu {
    /**
     * Properties
     */
    private String menuId;
    private String restId;

    /**
     * Constructor Methods
     */
    public Menu() {
        this.menuId = "";
        this.restId = "";
    }

    public Menu(String _menuId, String _restId) {
        this.menuId = _menuId;
        this.restId = _restId;
    }

    /**
     * Getter methods
     */
    public String getMenuId() {
        return this.menuId;
    }

    public String getRestId() {
        return this.restId;
    }

    /**
     * Methods operation
     */

    /**
     * createMenuId()
     *  - Create new id for menu
     * @param idNum
     * @return String
     */
    public static String createMenuId(Integer idNum) {
        String newMenuId;

        if (idNum < 10) {
            newMenuId = String.format("MENU00%d", idNum);
        }
        else if (idNum < 100) {
            newMenuId = String.format("MENU0%d", idNum);
        }
        else {
            newMenuId = String.format("MENU%d", idNum);
        }

        return newMenuId;
    }

    /**
     * loadMenu()
     *  - Load menu's information of a restaurant
     * @param document
     * @return Menu
     */
    public Menu loadMenu(Map<String, Object> document) {
        String _menuId = document.get("menu_id").toString();
        String _restId = document.get("rest_id").toString();

        return new Menu(_menuId, _restId);
    }

    /**
     * createMenuData()
     *  - Create Menu's data for query
     * @param _menuId
     * @param _restId
     * @return Map<String, Object>
     */
    public static Map<String, Object> createMenuData(String _menuId, String _restId) {
        Map<String, Object> document = new HashMap<>();

        document.put("menu_id", _menuId);
        document.put("rest_id", _restId);

        return document;
    }
}
