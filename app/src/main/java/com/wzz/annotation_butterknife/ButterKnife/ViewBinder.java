package com.wzz.annotation_butterknife.ButterKnife;

/**
 * 所有生成的类都要实现这个接口
 * @param <T>
 */
public interface ViewBinder<T> {
    void bind(T t);
}
