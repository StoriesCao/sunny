package com.stories.sunny.util;

import com.stories.sunny.R;

/**
 * Created by Charlottecao on 11/3/17.
 */

public class WeatherIconUtil {

    /**
     * 根据天气code获取相应的天气图标
     * @param code JSON中的天气coed
     * @return  Icon
     */
    public static int getIcon(int code) {
        if (code == 100) { // 晴
            return  R.drawable.ic_sunny;
        } else if (code >= 101  && code <= 104) { //阴
            return R.drawable.ic_cloud;
        } else if (code >= 200 && code <= 213) { //风
            return R.drawable.ic_wind;
        } else if ((code >= 300 && code <= 301) || code == 305 || code == 309) { //阵雨
            return R.drawable.ic_light_rain;
        } else if (code >= 302 && code <= 304) { //雷阵雨
            return R.drawable.ic_thunder;
        } else if ((code >= 306 && code <= 308) || (code >= 310 && code <= 313)) { //大（暴）雨
            return R.drawable.ic_heavy_rain;
        } else if (code == 400 || code == 406 || code == 407) { //小雪 阵雪
            return R.drawable.ic_light_snow;
        } else if (code == 401) { //中雪
            return R.drawable.ic_medium_snow;
        } else if (code >= 402 && code <= 405) { //大雪
            return R.drawable.ic_heavy_snow;
        } else if (code >= 500 && code <= 508) { //雾 、 霾
            return R.drawable.ic_fog;
        } else {
            return R.drawable.ic_unknown;
        }
    }
    
}
