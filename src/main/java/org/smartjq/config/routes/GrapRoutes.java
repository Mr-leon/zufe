package org.smartjq.config.routes;

import com.jfinal.config.Routes;

public class GrapRoutes extends Routes{

    @Override
    public void config() {
        setBaseViewPath("/WEB-INF/admin/grap");
    }
}
