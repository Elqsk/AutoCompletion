package com.example.auto_complete;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements TextWatcher {

    EditText editText;

    // 총 데이터 집합
    ArrayList<String> dataSet;

    // 표시할 데이터 집합
    // 총 데이터 집합에 사용자가 입력한 문자열과 일치하는 데이터가 있으면 검색어를 추천해 준다.
    ArrayList<SpannableStringBuilder> suggestionsSet;

    // 자동 완성(제안된 검색어) 목록
    // https://developer.android.com/guide/topics/ui/layout/recyclerview#java
    RecyclerView suggesterView;
    SuggesterViewAdapter adapter;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.edit_text);

        editText.addTextChangedListener(this);

        /*
         * 전체 데이터 집합
         * 리스트 아이템 데이터 하드 코딩(임의로 삽입)
         */
        dataSet = new ArrayList<>();
        dataSet.add("마이샵");
        dataSet.add("마이카");
        dataSet.add("마이오토");
        dataSet.add("마이너스");
        dataSet.add("대출마이너스");
        dataSet.add("마이샵안내");
        dataSet.add("마이샵혜택");
        dataSet.add("마이신");
        dataSet.add("마이신한포인트");
        dataSet.add("프리미엄마이카");

        /*
         * 제안된 검색어 데이터 집합
         * 총 데이터 집합에 사용자가 입력한 문자열과 일치하는 데이터가 있으면 검색어를 추천해 준다.
         * 일치하는 데이터만 골라서 따로 담아두었다가 나열한다.
         */
        suggestionsSet = new ArrayList<>();

        /*
         * 제안된 검색어 데이터를 나열할 목록
         * https://developer.android.com/guide/topics/ui/layout/recyclerview#java
         */
        suggesterView = findViewById(R.id.suggester_view);
        suggesterView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        suggesterView.setLayoutManager(layoutManager);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    /*
     * 입력란 감지
     */
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        Log.d("kkang", "Main Activity / onTextChanged(...) / 입력된 문자열: " + s);
        Log.d("kkang", "Main Activity / onTextChanged(...) / 새로 추가된 문자 시작 인덱스: " + start);
        Log.d("kkang", "Main Activity / onTextChanged(...) / 삭제된 기존 문자 수: " + before);
        Log.d("kkang", "Main Activity / onTextChanged(...) / 새로 추가된 문자 수: " + count);
        Log.d("kkang", " ");

        /*
         * 데이터를 새로 채워넣기 위해 기존의 제안된 검색어 데이터 집합을 비운다.
         */
        suggestionsSet.clear();

        /*
         * 사용자가 입력한 문자열을 포함하는 검색어가 있는지 전체 데이터 집합에서 하나씩 검사한다.
         */
        if (!editText.getText().toString().equals("")) {
            for (String string : dataSet) {
                Log.d("kkang", "Main Activity / onTextChanged(...) / for (String string : dataSet) / String string: " + string);

                /*
                 * 사용자가 입력한 문자열을 포함하는 데이터일 경우 ...
                 */
                if (string.contains(s)) {
                    int _start = string.indexOf(s.toString());
                    int _end = _start + s.length();

                    Log.d("kkang", "Main Activity / onTextChanged(...) / string.contains(s) / int _start: " + _start);
                    Log.d("kkang", "Main Activity / onTextChanged(...) / string.contains(s) / int _end: " + _end);

                    /*
                     * 일치하는 문자열에 글자 파란 색과 진하게를 적용하고, 제안된 검색어 데이터 집합에 삽입한다.
                     */
                    SpannableStringBuilder builder = new SpannableStringBuilder(string);
                    builder.setSpan(new ForegroundColorSpan(Color.BLUE), _start, _end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    builder.setSpan(new StyleSpan(Typeface.BOLD), _start, _end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    suggestionsSet.add(builder);
                }
            }
        }


        /*
         * 제안할 검색어 데이터가 있으면 목록을 나열한다.
         */
        if (suggestionsSet.size() != 0) {
            if (adapter == null) {
                adapter = new SuggesterViewAdapter(suggestionsSet);
                suggesterView.setAdapter(adapter);

                adapter.setOnClickListener(new OnSuggesterViewItemClickListener() {
                    @Override
                    public void onClick(int position) {
                        Log.d("kkang", "Main Activity / onTextChanged(...) / onClick(...) position: " + position);
                        Log.d("kkang", "Main Activity / onTextChanged(...) / onClick(...) dataSet.get(position): " + suggestionsSet.get(position));

                        // 목록 아이템을 클릭하면 텍스트를 자동 완성한다.
                        // "마이" 입력 → 목록에서 "마이너스" 클릭 → 입력창의 텍스트를 "마이너스"로 자동 완성
                        SpannableStringBuilder builder = new SpannableStringBuilder(suggestionsSet.get(position));
                        builder.clearSpans();
                        editText.setText(builder);
                        // 커서는 텍스트 끝에다 가져다 놓는다.
                        editText.setSelection(editText.getText().length());

                        Log.d("kkang", " ");
                    }
                });
            } else {
                adapter.notifyDataSetChanged();
            }

            // 데이터가 잘 들어있는지 확인하는 로그
            for (SpannableStringBuilder builder : suggestionsSet) {
                Log.d("kkang", "Main Activity / onTextChanged(...) / for (SpannableStringBuilder builder : suggestionsSet) / builder: " + builder);
            }
        } else {
            /*
             * 제안할 검색어가 없으면 빈 목록을 출력한다.
             */
            if (adapter != null) adapter.notifyDataSetChanged();
        }
        Log.d("kkang", " ");
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}