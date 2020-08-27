package com.example.recipeactivity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.kakao.auth.AuthType;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.kakao.usermgmt.callback.MeV2ResponseCallback;
import com.kakao.usermgmt.response.MeV2Response;
import com.kakao.usermgmt.response.model.Profile;
import com.kakao.usermgmt.response.model.UserAccount;
import com.kakao.util.OptionalBoolean;
import com.kakao.util.exception.KakaoException;

public class KakaoLoginActivity extends AppCompatActivity {

    private SessionCallback sessionCallback;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kakao_login);
        getSupportActionBar().hide();

        sessionCallback = new SessionCallback();
        Session.getCurrentSession().addCallback(sessionCallback);
        Session.getCurrentSession().checkAndImplicitOpen(); // 자동 로그인
        // 현재 앱에 유효한 로그인 토큰이 있다면 바로 로그인을 시켜주는 함수이다.
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // 세션 콜백 삭제
        Session.getCurrentSession().removeCallback(sessionCallback);
        // 네이버, 구글 등 다른 로그인 API 를 사용하는 경우 이 콜백 제거 를 안해주면
        // 로그아웃 작업에 문제가 생긴다.

    }

    @Override //카카오톡 로그인 액티비티에서 결과값을 가져오기 위해 필요하다.
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //카카오톡 간편로그인 실행 결과를 받아서 SDK로 전달
        if(Session.getCurrentSession().handleActivityResult(requestCode,resultCode,data))
        {
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private class SessionCallback implements ISessionCallback {

        // 로그인에 성공한 상태 // 로그인 세션이 열렸을 때
        @Override
        public void onSessionOpened() {

            UserManagement.getInstance()
                    .me(new MeV2ResponseCallback() {
                        @Override
                        public void onSessionClosed(ErrorResult errorResult) {
                            // 로그인 도중 세션이 비정상적인 이유로 닫혔을 때
                            Log.e("KAKAO_API","세션이 닫혀 있음: "+errorResult);
                        }

                        @Override
                        public void onFailure(ErrorResult errorResult) {
                            // 로그인 실패했을 때, 인터넷 연결 불안정한 경우도 여기 해당
                            // 인터넷 연결 오류 같은 경우 알려주는게 좋다.

                            Log.e("KAKAO_API","사용자 정보 요청 실패: "+errorResult);
                        }

                        // 사용자 요청에 성공한 경우
                        @Override
                        public void onSuccess(MeV2Response result) {
                            // 로그인에 성공했을 때
                            Log.i("KAKAO_API","사용자 아이디: "+result.getId());

                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.putExtra("name",String.valueOf(result.getId()));
                            intent.putExtra("profile",result.getProfileImagePath());
                            startActivity(intent);
                            finish();

                            UserAccount kakaoAccount = result.getKakaoAccount();
                            if(kakaoAccount != null)
                            {
                                //이메일
                                String email = kakaoAccount.getEmail();

                                if(email != null)
                                {
                                    Log.i("KAKAO_API","email: "+email);
                                }else if(kakaoAccount.emailNeedsAgreement() == OptionalBoolean.TRUE)
                                {
                                    // 동의 요청 후 이메일 획득 가능
                                    // 단, 선택 동의로 설정되어 있다면 서비스 이용 시나리오 상에서 반드시 필요
                                }else
                                {
                                    //이메일 획득 불가
                                }

                                //프로필
                                Profile profile = kakaoAccount.getProfile();

                                if(profile != null)
                                {
                                    Log.d("KAKAO_API","nickname: "+profile.getNickname());
                                    Log.d("KAKAO_API","profile image: "+profile.getProfileImageUrl());
                                    Log.d("KAKAO_API","thumbnail image: "+profile.getThumbnailImageUrl());
                                } else if(kakaoAccount.profileNeedsAgreement() == OptionalBoolean.TRUE)
                                {
                                    // 동의 요청 후 프로필 정보 획득 가능
                                }else
                                {
                                    //프로필 획득 불가
                                }
                            }
                        }
                    });
        }

        // 로그인에 실패한 상태 // 로그인 세션이 정상적으로 열리지 않았을 때
        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            Log.e("SessionCallback :: ", "onSessionOpenFailed : " + exception.getMessage());

        }

    }
}
