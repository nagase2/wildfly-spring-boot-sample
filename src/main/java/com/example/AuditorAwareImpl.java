package com.example;

import org.springframework.data.domain.AuditorAware;

public class AuditorAwareImpl implements AuditorAware<Object> {



    /*
     * 現在のユーザIDを取得する。
     */
    @Override
    public String getCurrentAuditor() {
      //TODO:ここで固定値を返しているが、実際は現在のユーザを取得してユーザIDを変更する必要有り
        return "aaaaaa";
    }
}
