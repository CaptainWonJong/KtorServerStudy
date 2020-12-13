package com.wonjong.lee.exception

import com.wonjong.lee.model.ExceptionVo

/**
 * @author leewonjong@doinglab.com
 */
class InvalidCredentialsException(exception: ExceptionVo) : RuntimeException(exception.message)