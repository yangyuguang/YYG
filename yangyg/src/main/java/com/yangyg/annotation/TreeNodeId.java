package com.yangyg.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ע��
 * @Target  ָ��ע����ӵ�λ�� �����ԡ��ࡢ�����ȡ�������
 * @Retention  ע����ע����ʲôʱ��ɼ�(����ʱ�ɼ� )
 * @author Administrator
 *
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TreeNodeId {

}
