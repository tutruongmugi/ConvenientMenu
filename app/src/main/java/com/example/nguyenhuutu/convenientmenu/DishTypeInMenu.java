package com.example.nguyenhuutu.convenientmenu;

import java.util.HashMap;
import java.util.Map;

public class DishTypeInMenu {
    /**
     * Properties
     */
    private String menuId;
    private String typeId;

    /**
     * Constructor methods
     */
    public DishTypeInMenu(String _menuId, String _typeId) {
        this.menuId = _menuId;
        this.typeId = _typeId;
    }

    /**
     * Getter methods
     */
    public String getMenuId() {
        return this.menuId;
    }

    public String getDishTypeId() {
        return this.typeId;
    }

    /**
     * Other methods
     */

    /**
     * createDishTypeInMenuId()
     *  - Create new id for DishTypeInMenu
     * @param idNum
     * @return String
     */
    public static String createDishTypeInMenuId(Integer idNum) {
        return String.format("DTINMENU%d", idNum);
    }

    /**
     * loadDishTypeInMenu()
     *  - Load DishTypeInMenu
     * @param document
     * @return DishTypeInMenu
     */
    public static DishTypeInMenu loadDishTypeInMenu(Map<String, Object> document) {
        String _menuId = document.get("menu_id").toString();
        String _typeId = document.get("type_id").toString();

        return new DishTypeInMenu(_menuId, _typeId);
    }

    /**
     * createDishTypeInMenuData()
     *  - Create DishTypeInMenu's data for query
     * @param _menuId
     * @param _typeId
     * @return Map<String, Object>
     */
    public static Map<String, Object> createDishTypeInMenuData(String _menuId, String _typeId) {
        Map<String, Object> document = new HashMap<>();

        document.put("menu_id", _menuId);
        document.put("type_id", _typeId);

        return document;
    }
}
