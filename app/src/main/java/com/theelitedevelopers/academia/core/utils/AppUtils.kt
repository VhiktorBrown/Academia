package com.theelitedevelopers.academia.core.utils

import android.annotation.SuppressLint
import android.content.Context
import com.theelitedevelopers.academia.core.data.local.SharedPref
import com.theelitedevelopers.academia.modules.authentication.data.models.Student
import java.lang.String.format
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class AppUtils {

    companion object {

        private const val SECOND_MILLIS = 1000
        private const val MINUTE_MILLIS = 60 * SECOND_MILLIS
        private const val HOUR_MILLIS = 60 * MINUTE_MILLIS
        private const val DAY_MILLIS = 24 * HOUR_MILLIS
        private const val WEEK_MILLIS = 7 * DAY_MILLIS

        fun getFirstNameOnly(fullName: String): String {
            val split = fullName.split(" ").toTypedArray()
            return split[0]
        }

        //This function is primarily for the buzz bubbles- to handle date Manipulation
        fun getSingleInboxDate(date: String): String? {
            var buzzDate: Date? = null
            var finalDate: String? = null
            try {
                buzzDate = Objects.requireNonNull(
                    getTimeFormat().parse(date)
                )
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            finalDate = when {
                isTheSameDay(date) -> {
                    getHourMinuteFormat().format(buzzDate!!)
                }
                withinAWeek(date) -> {
                    getDayFormat()
                        .format(buzzDate!!) + ", " + getHourMinuteFormat()
                        .format(buzzDate)
                }
                isTheSameYear(date) -> {
                    getDayMonthFormat()
                        .format(buzzDate!!) + ", " + getHourMinuteFormat()
                        .format(buzzDate)
                }
                else -> {
                    getDayMonthYearFormat()
                        .format(buzzDate!!) + ", " + getHourMinuteFormat()
                        .format(buzzDate)
                }
            }
            return finalDate
        }

        fun getTimeAgo(time: Long): String? {
            var time = time
            if (time < 1000000000000L) {
                // if timestamp given in seconds, convert to millis
                time *= 1000
            }
            val now = System.currentTimeMillis()
            if (now > time || time <= 0) {
                return "Time's up"
            }

            // TODO: localize
            val diff = time - now
            return when {
                diff < MINUTE_MILLIS -> {
                    "Time's up"
                }
                diff < 2 * MINUTE_MILLIS -> {
                    "1 min left"
                }
                diff < 50 * MINUTE_MILLIS -> {
                    (diff / MINUTE_MILLIS).toString() + " minutes left"
                }
                diff < 110 * MINUTE_MILLIS -> {
                    "1 hour left"
                }
                diff < 24 * HOUR_MILLIS -> {
                    (diff / HOUR_MILLIS).toString() + " hours left"
                }
                diff < 48 * HOUR_MILLIS -> {
                    "1 day left"
                }
                diff < 7 * DAY_MILLIS -> {
                    (diff / DAY_MILLIS).toString() + " days left"
                }
                diff < 2 * WEEK_MILLIS -> {
                    "a week left"
                }
                else -> {
                    (diff / WEEK_MILLIS).toString() + " weeks left"
                }
            }
        }

        @SuppressLint("SimpleDateFormat")
        fun convertDateToPresentableFormat(dateInString: String): String {
            val simpleDateFormat = SimpleDateFormat("dd MMM yyyy")
            val simpleTimeFormat = SimpleDateFormat("hh:mm aa")
            var date: Date? = null
            var dateInPresentableFormat: String? = null
            val format = SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss")
            try {
                date = format.parse(dateInString)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            dateInPresentableFormat = if (date != null) {
                simpleDateFormat.format(date) +
                        ". " + simpleTimeFormat.format(date)
            } else {
                ""
            }
            return dateInPresentableFormat
        }

        @SuppressLint("SimpleDateFormat")
        fun convertDateToPresentableFormatWithOnlyDate(dateInString: String): String? {
            val simpleDateFormat = SimpleDateFormat("dd MMM yyyy")
            var date: Date? = null
            var dateInPresentableFormat: String? = null
            val format = SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss")
            try {
                date = format.parse(dateInString)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            dateInPresentableFormat = if (date != null) {
                simpleDateFormat.format(date)
            } else {
                ""
            }
            return dateInPresentableFormat
        }

        fun getTimeInDaysOrWeeks(date: String): String? {
            var date1: Date? = null
            var timeInMillis: Long = 0
            val format = SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss")
            try {
                date1 = format.parse(date)
                timeInMillis = date1.time
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return getTimeAgo(timeInMillis)
        }


        fun convertDateFromOneFormatToAnother(
            sourceFormat: String?,
            destinationFormat: String?,
            date: String
        ): String {
            @SuppressLint("SimpleDateFormat") val sourceDateFormat = SimpleDateFormat(sourceFormat)
            @SuppressLint("SimpleDateFormat") val destinationDateFormat =
                SimpleDateFormat(destinationFormat)
            var newDate: String = ""
            try {
                val sourceDate = sourceDateFormat.parse(date)
                newDate = destinationDateFormat.format(Objects.requireNonNull(sourceDate))
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            return newDate
        }

        @Throws(ParseException::class)
        fun convertToDateFormat(formatType: String?, dateInString: String): Date? {
            @SuppressLint("SimpleDateFormat") val format = SimpleDateFormat(formatType)
            return format.parse(dateInString)
        }


        fun getInboxDate(date: String): String? {
            @SuppressLint("SimpleDateFormat") val format = SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss")
            var d: String? = ""
            var date1: Date? = null
            try {
                date1 = format.parse(date)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            if (date1 != null) {
                //check if date is the same with current date
                d = if (isTheSameDay(date)) {
                    getHourMinuteFormat().format(date1)
                } else if (isTheSameYear(date)) {
                    if (withinAWeek(date)) {
                        getDayFormat().format(date1)
                    } else {
                        getDayMonthFormat().format(date1)
                    }
                } else {
                    getDayMonthYearFormat().format(date1)
                }
            }
            return d
        }


        fun isTheSameDay(date: String): Boolean {
            var parsedDate: Date? = null
            var newDayToday: Date? = null
            var isSameDay = false
            try {
                val initialDate = Objects.requireNonNull(getTimeFormat().parse(date))

                //Here, I changed the Dates into the Form 'dd MMM yyyy'
                val formattedDate = getDayMonthYearFormat().format(initialDate)

                //then parsed it back to a Normal Date object.
                parsedDate = getDayMonthYearFormat().parse(formattedDate)

                //TODO Check later. Something seems off about this line.
                newDayToday = getDayMonthYearFormat().parse(getDayMonthYearFormat().format(Date()))

                //check if they are the same
                if (Objects.requireNonNull(parsedDate) == newDayToday) {
                    isSameDay = true
                }
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            return isSameDay
        }


        //Function to check if date has the same year with current date
        fun isTheSameYear(date: String): Boolean {
            @SuppressLint("SimpleDateFormat") val simpleDateFormat = SimpleDateFormat("yyyy")
            @SuppressLint("SimpleDateFormat") val format = SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss")
            @SuppressLint("SimpleDateFormat") val sourceFormat =
                SimpleDateFormat("EEE MMM d HH:mm:ss z yyyy")
            var parsedDate: Date? = null
            var newDayToday: Date? = null
            var isSameYear = false
            try {
                //Here, I changed the Dates into the Form 'yyyy'
                val initialDate = Objects.requireNonNull(format.parse(date))
                val formattedDate = getYearFormat().format(initialDate)
                parsedDate = getYearFormat().parse(formattedDate)
                newDayToday = getYearFormat().parse(getYearFormat().format(Date()))

                //check if they are the same
                if (Objects.requireNonNull(parsedDate) == newDayToday) {
                    isSameYear = true
                }
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            return isSameYear
        }


        //This is the Function for checking if the time passed is 1 week from current date
        fun withinAWeek(date: String): Boolean {
            var time: Long = 0
            try {
                time = Objects.requireNonNull(getTimeFormat().parse(date)).time
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            if (time < 1000000000000L) {
                // if timestamp given in seconds, convert to millis
                time *= 1000
            }
            val now = System.currentTimeMillis()

            // TODO: localize
            val diff = now - time
            return diff < 7 * AppUtils.DAY_MILLIS
        }

        fun saveDataToSharedPref(context: Context, student: Student) {
            SharedPref.getInstance(context).saveString(Constants.ID, student.id)
            SharedPref.getInstance(context).saveString(Constants.NAME, student.fullName)
            SharedPref.getInstance(context).saveString(Constants.EMAIL, student.email)
            SharedPref.getInstance(context).saveString(Constants.REG_NUMBER, student.regNumber)
            SharedPref.getInstance(context).saveString(Constants.DATE_OF_BIRTH, student.dateOfBirth)
            SharedPref.getInstance(context).saveString(Constants.DEPARTMENT, student.department)
            SharedPref.getInstance(context).saveString(Constants.HOSTEL, student.hostel)
            SharedPref.getInstance(context).saveString(Constants.LEVEL, student.level)
            SharedPref.getInstance(context).saveBoolean(Constants.REP, student.rep)
            SharedPref.getInstance(context).saveString(Constants.PHONE_NUMBER, student.phoneNumber)
            SharedPref.getInstance(context).saveString(Constants.GENDER, student.gender)
            SharedPref.getInstance(context).saveString(Constants.UID, student.uid)
            SharedPref.getInstance(context).saveString(Constants.TOKEN, student.token)
        }


        fun removeDataToSharedPref(context : Context) {
            SharedPref.getInstance(context).removeKeyValue(Constants.ID)
            SharedPref.getInstance(context).removeKeyValue(Constants.NAME)
            SharedPref.getInstance(context).removeKeyValue(Constants.EMAIL)
            SharedPref.getInstance(context).removeKeyValue(Constants.REG_NUMBER)
            SharedPref.getInstance(context).removeKeyValue(Constants.DATE_OF_BIRTH)
            SharedPref.getInstance(context).removeKeyValue(Constants.DEPARTMENT)
            SharedPref.getInstance(context).removeKeyValue(Constants.HOSTEL)
            SharedPref.getInstance(context).removeKeyValue(Constants.LEVEL)
            SharedPref.getInstance(context).removeKeyValue(Constants.REP)
            SharedPref.getInstance(context).removeKeyValue(Constants.PHONE_NUMBER)
            SharedPref.getInstance(context).removeKeyValue(Constants.GENDER)
            SharedPref.getInstance(context).removeKeyValue(Constants.UID)
            SharedPref.getInstance(context).removeKeyValue(Constants.TOKEN)
        }

        @SuppressLint("SimpleDateFormat")
        fun getTimeFormat(): SimpleDateFormat {
            return SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss")
        }

        @SuppressLint("SimpleDateFormat")
        fun fromTimeStampToString(timeStamp: Long): String {
            var stamp = timeStamp;
            stamp *= 1000L;
            return try {
                val sdf = SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss")
                val netDate = Date(stamp)
                sdf.format(netDate)
            } catch (ex: Exception) {
                "xx"
            }
        }

        @Throws(ParseException::class)
        fun convertToDateWithoutSeconds(dateInString: String): Date? {
            val date = convertDateFromOneFormatToAnother(
                "EEE, d MMM yyyy HH:mm:ss",
                "EEE, d MMM yyyy HH:mm",
                dateInString
            )
            return getTimeFormatWithoutSeconds()
                .parse(date)
        }

        fun getDestinationFormat(): String {
            return "EEE, d MMM yyyy HH:mm:ss";
        }

        @SuppressLint("SimpleDateFormat")
        fun getTimeFormatWithoutSeconds(): SimpleDateFormat {
            return SimpleDateFormat("EEE, d MMM yyyy HH:mm")
        }

        @SuppressLint("SimpleDateFormat")
        fun getDayFormat(): SimpleDateFormat {
            return SimpleDateFormat("EEE")
        }

        @SuppressLint("SimpleDateFormat")
        fun getDayMonthFormat(): SimpleDateFormat {
            return SimpleDateFormat("dd MMM")
        }

        @SuppressLint("SimpleDateFormat")
        fun getYearFormat(): SimpleDateFormat {
            return SimpleDateFormat("yyyy")
        }

        @SuppressLint("SimpleDateFormat")
        fun getDayMonthYearFormat(): SimpleDateFormat {
            return SimpleDateFormat("dd MMM yyyy")
        }

        @SuppressLint("SimpleDateFormat")
        fun getHourMinuteFormat(): SimpleDateFormat {
            return SimpleDateFormat("hh:mm aa")
        }


    }
}