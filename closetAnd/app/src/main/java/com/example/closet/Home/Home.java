package com.example.closet.Home;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;


import com.example.closet.GetaLocation;
import com.example.closet.Clothes.Clothes;
import com.example.closet.LogIn;
import com.example.closet.R;
import com.example.closet.SignUp;
import com.example.closet.openCV_test;
import com.example.closet.storeClothingNetworking;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;

/**
 * A simple {@link Fragment} subclass.
 */
public class Home extends Fragment implements View.OnClickListener {
    private static final int GET_WEATHER_IS_OK = 3000;

    View view;
    Button singUp, logIn;
    ImageButton BtnMove;
    Button map;
    TextView weather_info_textView;

    Button testCv;

    public Home() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);
        map = (Button) view.findViewById(R.id.mapButton);
        map.setOnClickListener(this);
        singUp = (Button) view.findViewById(R.id.signUp);
        BtnMove = (ImageButton) view.findViewById(R.id.BtnActivityOne);

        singUp.setOnClickListener(this);
        BtnMove.setOnClickListener(this);

        logIn = (Button) view.findViewById(R.id.login_button);
        logIn.setOnClickListener(this);

        weather_info_textView = (TextView)view.findViewById(R.id.weather_text);

        testCv = (Button)view.findViewById(R.id.testCV);
        testCv.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        //로그인 , 회원가입하고 startForResult로 uid받기 !!
        if (view == singUp) {
            Intent intent = new Intent(getActivity(), SignUp.class);
            startActivity(intent); //requestCode상수로 만들기
        } else if (view == map) {
            Intent intent = new Intent(getActivity(), GetaLocation.class);
            //startActivity(intent);
            startActivityForResult(intent,GET_WEATHER_IS_OK);
            //startActivitiyForReuslt =Activity로 호출받게 됨.
            //Fragment를 불러들인 mainActivity가 있다면 거기서 받을 수가 있겠죠.
            //메인에서 받은 값을 Fragment에서 재 캐치하는 방법으로 했었던게 기억납니다.
        } else if (view == logIn) {
            Intent intent = new Intent(getActivity(), LogIn.class);
            startActivity(intent);
        } else if (view == BtnMove) {
            Intent intent = new Intent(getActivity(), Clothes.class);
            startActivityForResult(intent, 30);
        }else if (view == testCv){
            Intent intent = new Intent(getActivity(), openCV_test.class);
            startActivity(intent);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 30) {
            Log.d("Log.dSD", "회원가입 완료");
        } else if (requestCode == GET_WEATHER_IS_OK) {
            Log.d("Log_DDSF", "위치 얻기 완료");
            String weather_info = data.getStringExtra("result");
            Log.d("Log_dHomeWeather",weather_info);
            setWeather_info(weather_info);

        }
    }

    private  void setWeather_info(String weatherInfo){
        try {
            //JSONArray jsonArray = new JSONArray(weatherInfo); {}
            JSONObject jsonObject = new JSONObject(weatherInfo);
            //Log.d("JSONAOBJECT",jsonObject.toString());
            JSONObject grid = (JSONObject) jsonObject.get("grid");
            JSONObject wind = (JSONObject) jsonObject.get("wind");
            JSONObject temp = (JSONObject) jsonObject.get("temperature");
            String humidity = (String) jsonObject.get("humidity");
            String timeRelease = (String) jsonObject.get("timeRelease");

            /*Log.d("JSONGRID",grid.toString());
            Log.d("JSONWIND",wind.toString());
            Log.d("JSONTEMP",temp.toString());
            Log.d("JSONHUMI",humidity);
            Log.d("JSONTIME",timeRelease);
            */
            String [] cold = new String[] {"밖이 추워요! 외투를 잊지 마세요~", "밖이 추워요! 코트를 잊지 마세요.", "한파 주위! 롱패딩은 필수에요~"};
            String [] warm = new String[] {"날이 따스하네요. 소풍 가기 좋은 날이에요!", "비교적 따뜻한 날씨입니다. 가벼운 옷차림을 추천해요!"};
            String [] hot = new String [] {"너무 더운 날이에요. 물을 자주 드세요!", "폭염 주의! 모자와 선글라스를 챙기시는 게 어떠세요?"};
            String advice = null; String result = null;

            int rv = (int)(Math.random()*3);
            String stmax = temp.get("tmax").toString();
            double tmax = Double.parseDouble(stmax);
            if(tmax < 0)
                advice = cold[rv];
            else if(tmax < 20)
                advice = warm[rv];

            String wea = (String) grid.get("city") + " " + grid.get("county") + "의 현재 날씨입니다.\n최고기온: "+temp.get("tmax")+", 최저기온 : "+temp.get("tmin")+"\n기준시간 : "+timeRelease + "\n";
            result = wea + advice;
            weather_info_textView.setText(result);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}