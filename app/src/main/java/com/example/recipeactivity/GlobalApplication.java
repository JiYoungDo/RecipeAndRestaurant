package com.example.recipeactivity;

import android.app.Application;
import android.content.Context;

import androidx.annotation.Nullable;

import com.kakao.auth.ApprovalType;
import com.kakao.auth.AuthType;
import com.kakao.auth.IApplicationConfig;
import com.kakao.auth.ISessionConfig;
import com.kakao.auth.KakaoAdapter;
import com.kakao.auth.KakaoSDK;

// 카카오 SDK 사용하기 위해서는 초기화를 해줘야 한다.
// 초기화는 GlobalApplication 공유 클래스를 만들어 앱 수준에서 관리하도록 하겠다.
// kakaoSDKAdpater 클래스는 KakaoAdapter 를 상속받는다.
public class GlobalApplication extends Application {

    private  static volatile GlobalApplication instance = null;

    public class KakaoSDKAdapter extends KakaoAdapter
    {
        @Override // 카카오 로그인 세션을 불러올 때의 설정값을 설정하는 부분
        public ISessionConfig getSessionConfig() {
            return new ISessionConfig() {
                @Override
                public AuthType[] getAuthTypes() {
                    return new AuthType[] {AuthType.KAKAO_LOGIN_ALL};
                    /*
                    로그인을 하는 방식을 지정하는 부분이다.
                    AuthType으로는 4가지 방식이 있다.
                    1) KAKAO_TALK : 카카오톡으로 로그인
                    2) KAKAO_STORY : 카카오 스토리로 로그인
                    3) KAKAO_ACOUNT : 웹뷰를 통한 로그인
                    4) KAKAO_TALK_EXCLUDE_NATIVE_LOGIN :  카카오톡으로만 로그인+ 계정 없으면 계정 생성 버튼 제공
                    5) KAKAO_LOGIN_ALL: 모든 로그인 방식 사용 가능. 정확히는, 카카오톡이나 카카오 스토리 있으면 그쪽으로 로그인 기능제공하고, 둘 다 없으면 웹뷰
                    * */

                }

                @Override
                public boolean isUsingWebviewTimer() {
                    return false;
                }

                @Override
                public boolean isSecureMode() {
                    return false;
                }

                @Nullable
                @Override
                public ApprovalType getApprovalType() {
                    return ApprovalType.INDIVIDUAL;
                }

                @Override
                public boolean isSaveFormData() {
                    return true;
                }
            };
        }

        // Application 이 가지고 있는 정보를 얻기 위한 인터페이스
        @Override
        public IApplicationConfig getApplicationConfig() {
            return new IApplicationConfig() {
                @Override
                public Context getApplicationContext() {
                    return GlobalApplication.getGlobalApplicationContext();
                }
            };
        }
    }

    public static GlobalApplication getGlobalApplicationContext()
    {
        if(instance == null)
        {
            throw new IllegalStateException("This Application does not inherit com.kakao.GlobalApplication");
        }
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        // Kakao Sdk 초기화
        KakaoSDK.init(new KakaoSDKAdapter());
    }

    public void onTerminate()
    {
        super.onTerminate();
        instance= null;
    }
}
