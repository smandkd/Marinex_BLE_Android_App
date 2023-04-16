package org.techtowm.retrofit2.ui.join;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import org.techtowm.retrofit2.Data.UserDTO;

import java.util.List;
import java.util.regex.Pattern;

public class JoinViewModel {
    MutableLiveData<String> duplicateIdMutableData = new MutableLiveData<>();
    MutableLiveData<String> validPWMutableData = new MutableLiveData<>();
    MutableLiveData<Boolean> joinResult = new MutableLiveData<>();
    JoinRepository joinRepository;

    public JoinViewModel() {
        joinRepository = new JoinRepository();
    }

    public void checkDuplicateId(String s) {
        joinRepository.checkDuplicateRemote(new JoinRepository.IcheckResponse() {
            @Override
            public void onResponse(List<UserDTO> list) {
                for( int i = 0; i < list.size(); i++ ) {
                    if( s.equals( list.get(i).getID() ) ) {
                        duplicateIdMutableData.postValue("중복된 아이디입니다.");
                        //Log.d("SangJoinVM", String.valueOf(s.equals( list.get(i).getID() )));
                    }
                    else {
                        //Log.d("SangJoinVM", String.valueOf(s.equals( list.get(i).getID() )));
                    }
                }

                //
            }

            @Override
            public void onFailure(Throwable t) {
                duplicateIdMutableData.postValue("아이디를 다시 입력해주십시오.");
            }
        });
    }

    public void join(String id, String password, String name, String age) {

        joinRepository.joinRemote(id, password, name, age, new JoinRepository.IJoinResponse() {
            @Override
            public void onResponse(UserDTO userDTO) {
                if (userDTO.getToken() == 1) {
                    Log.d("SangJoinVM",  " join success " + userDTO.getToken() );
                    joinResult.setValue(true);
                }
                else {
                    joinResult.setValue(false);
                }
            }

            @Override
            public void onFaliure(Throwable t) {
                Log.d("SangJoinVM", "" + t + " join fail");
                joinResult.setValue(false);
            }
        });
    }

    public void isPasswordValid(String password) {
        // password must be over less than 7 chars include number and not empty
        if(password != null && password.trim().length() >= 7 && Pattern.matches("^[a-zA-Z0-9]*$", password) ) {
           // validPWMutableData.postValue("");
        }
        else {
            validPWMutableData.postValue("영문/숫자 조합 7자리 이상이여야합니다.");
        }
    }

    public MutableLiveData<Boolean> getJoinResult() {
        return joinResult;
    }

    public LiveData<String> getDuplicateResult() {
        return duplicateIdMutableData;
    }

    public LiveData<String> getPwValidResult() {
        return validPWMutableData;
    }

}