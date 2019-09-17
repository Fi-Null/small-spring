package com.small.spring.beans.io;

import java.io.IOException;
import java.io.InputStream;

/**
 * @InterfaceName Resource
 * @Description Resource是spring内部定位资源的接口。
 * @Author xiangke
 * @Date 2019/9/17 23:44
 * @Version 1.0
 **/
public interface Resource {

    InputStream getInputStream() throws IOException;

}
