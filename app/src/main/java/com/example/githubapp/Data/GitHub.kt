package com.example.githubapp.Data

import java.text.SimpleDateFormat
import java.time.Period
import java.util.*
import java.util.concurrent.TimeUnit

class GitHub(Owner_name: String,
             Owner_img: String,
             Rep_name: String,
             Rep_description: String,
             created_at:String,
             Rep_stars: Int,
             Rep_Issues: Int,
             html:String
) {
    protected  val Owner_name:String =Owner_name
    protected  val Rep_name :String=Rep_name
    protected  val Rep_description:String=Rep_description
    protected  val Owner_img:String=Owner_img
    protected  val created_at:String=created_at
    protected  val Rep_stars:Int=Rep_stars
    protected  val Rep_Issues :Int=Rep_Issues
    protected val  htmlUrl :String=html
    fun Rep_name():String{
        if (this.Rep_name.isEmpty())
            return ""
            else
        return this.Rep_name
    }
    fun Rep_des():String{
        if(this.Rep_description.isEmpty())
            return ""
        else
        return this.Rep_description
    }
    fun Rep_stars():String{
        return "Stars: "+this.Rep_stars
    }
    fun Rep_Issues():String{
        return "Issues: "+this.Rep_Issues
    }
    fun Owner_img():String{return this.Owner_img}

    fun TimeandOwner():String{
        val x =created_at.split("T")
        val tem:String ="Submitted "+getdateinDay(x[0])+" ago by "+this.Owner_name
        return tem
    }
    fun Owner_name():String {return this.Owner_name}
    fun getUrl():String{return this.htmlUrl}

    /*Implement function for calculate date */

    fun getdateinDay(created_date:String):Long{
        val calendar : Calendar = Calendar.getInstance()
        val date : Date =calendar.time
        val df : SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val datenew :String =df.format(date)
        val firstDate = df.parse(created_date)//06-24-2017")
        val secondDate = df.parse(datenew)//06-30-2017
        val  diff = secondDate.time - firstDate.time
        val  time = TimeUnit.DAYS
        val  diffrence = time.convert(diff, TimeUnit.MILLISECONDS)
        return diffrence
    }

}