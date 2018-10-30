package com.goodmorningvoca.std.app

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.goodmorningvoca.std.app.adapter.DropDownAdapter
import com.goodmorningvoca.std.app.adapter.LevelDropDownAdapter
import com.goodmorningvoca.std.app.model.Level
import com.nex3z.togglebuttongroup.SingleSelectToggleGroup
import kotlinx.android.synthetic.main.activity_level.*

class LevelActivity : AppCompatActivity() {


    private var ctg : String = ""
    private var part : Int = 0
    private var seq  : Int = 0
    private var level : Int = 0
    private var isStudy : Boolean = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_level)



        supportActionBar?.title ="진도선택"
        supportActionBar?.setDisplayHomeAsUpEnabled(true )

        /// ## 1 카테고리 선택
        ctg_group.setOnCheckedChangeListener(
                SingleSelectToggleGroup.OnCheckedChangeListener{
                    group, checkedId ->
                        //Toast.makeText(applicationContext , " Click ${checkedId}" , Toast.LENGTH_SHORT).show()
                        when( checkedId){
                            R.id.choice_a->{
                                Toast.makeText(applicationContext, "ele position ", Toast.LENGTH_SHORT).show()
                                ctg = "ele"
                                btn_part4.visibility = View.GONE
                                btn_part5.visibility = View.GONE
                                btn_part6.visibility = View.GONE
                                btn_part7.visibility = View.GONE
                                btn_part8.visibility = View.GONE
                            }
                            R.id.choice_b->{
                                Toast.makeText(applicationContext, "mid position ", Toast.LENGTH_SHORT).show()
                                ctg = "mid"
                                btn_part4.visibility = View.VISIBLE
                                btn_part5.visibility = View.GONE
                                btn_part6.visibility = View.GONE
                                btn_part7.visibility = View.GONE
                                btn_part8.visibility = View.GONE
                            }
                            R.id.choice_c->{
                                Toast.makeText(applicationContext, "high position ", Toast.LENGTH_SHORT).show()
                                ctg = "high"
                                btn_part4.visibility = View.VISIBLE
                                btn_part5.visibility = View.VISIBLE
                                btn_part6.visibility = View.VISIBLE
                                btn_part7.visibility = View.VISIBLE
                                btn_part8.visibility = View.VISIBLE
                            }
                            else ->{
                                Toast.makeText(applicationContext, "sat position ", Toast.LENGTH_SHORT).show()
                                ctg = "sat"
                                btn_part4.visibility = View.VISIBLE
                                btn_part5.visibility = View.VISIBLE
                                btn_part6.visibility = View.VISIBLE
                                btn_part7.visibility = View.GONE
                                btn_part8.visibility = View.GONE
                            }
                        }
                }
         )


        ///// ## 2. PART 선택
        val onClickListener = View.OnClickListener { view ->
            when( view.id ){
                R.id.btn_part1 -> part = 1
                R.id.btn_part1 -> part = 2
                R.id.btn_part1 -> part = 3
                R.id.btn_part1 -> part = 4
                R.id.btn_part1 -> part = 5
                R.id.btn_part1 -> part = 6
                R.id.btn_part1 -> part = 7
                R.id.btn_part1 -> part = 8
            }
        }

        btn_part1.setOnClickListener( onClickListener )
        btn_part2.setOnClickListener( onClickListener )
        btn_part3.setOnClickListener( onClickListener )
        btn_part4.setOnClickListener( onClickListener )
        btn_part5.setOnClickListener( onClickListener )
        btn_part6.setOnClickListener( onClickListener )
        btn_part7.setOnClickListener( onClickListener )
        btn_part8.setOnClickListener( onClickListener )

        /// ##3  레벨 선택.
        val levelList :ArrayList<Level>  =  ArrayList()
        for (i in 1..50 ){
            levelList.add( Level("Level ${i}  ${((i-1)*10)+1} ~ ${(i*10)} " , null , i , false  ))
        }

        val levelSpinnerAdapter = LevelDropDownAdapter( this , levelList )
        levelSpinner.adapter = levelSpinnerAdapter
        levelSpinner?.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {

                var level = 0
                when(ctg){
                    "ele" -> level = 0
                    "mid" -> level = 150
                    "high" -> level = 350
                    "sat" -> level = 750
                }

                level += part*50
                level += position + 1
                Toast.makeText(parent.context, "Level position  position" + position.toString(), Toast.LENGTH_SHORT).show()

            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                Toast.makeText(parent.context, "position Nothing selected "  , Toast.LENGTH_SHORT).show()//
            }

        })

        //// ## 4번째 학습 , 시험 선택 ...
        radio_study_or_test.setOnCheckedChangeListener(
                RadioGroup.OnCheckedChangeListener{
                    group, checkedId ->
                    when(checkedId){
                        R.id.btn_study -> { isStudy = true}
                        else -> {isStudy = true
                        Toast.makeText( applicationContext , "시험은 준비중입니다." , Toast.LENGTH_SHORT  ).show()
                        }
                    }
                }
        )




    }
    private fun go(){


    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }





}
