package me.song.sys.system.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * @author Songwe
 * @since 2023/4/18 14:58
 */
@Data
public class MetaVo {

    /**
     * 该路由在侧边栏和面包屑中展示的名字
     */
    private String title;

    /**
     * 该路由的图标，对应路径src/assets/icons/svg
     */
    private String icon;

    /**
     * 设置为true，则不会被 <keep-alive>缓存
     */
    private boolean noCache;

    /**
     * 设置为 false,则不会在面包屑展示
     */
    private boolean breadcrumb;

    public MetaVo() {
    }

    public MetaVo(String title, String icon) {
        this.title = title;
        this.icon = icon;
        this.noCache = true;
        this.breadcrumb = true;
    }
}
