package org.jssvc.lib.ui.home.bean;

import java.io.Serializable;
import java.util.List;

/**
 * <pre>
 *     author : TOC_010
 *     time   : 2017/12/13
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class MenuHubBean implements Serializable {
    private String title;
    private List<MenuBean> menulist;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<MenuBean> getMenulist() {
        return menulist;
    }

    public void setMenulist(List<MenuBean> menulist) {
        this.menulist = menulist;
    }
}
