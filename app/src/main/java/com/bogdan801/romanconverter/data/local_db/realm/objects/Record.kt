package com.bogdan801.romanconverter.data.local_db.realm.objects

import com.bogdan801.romanconverter.domain.model.LeaderboardItem
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId
import java.time.LocalDate

class Record: RealmObject {
    @PrimaryKey
    var _id: ObjectId = ObjectId()
    var quizType: Int = 0
    var day: Int = 1
    var month: Int = 1
    var year: Int = 2002
    var count: Int = 0
    var score: Int = 0

    fun toLeaderboardItem(): LeaderboardItem = LeaderboardItem(
        id = _id.timestamp,
        date = LocalDate.of(year, month, day),
        count = count,
        score = score
    )
}

fun LeaderboardItem.toRecordRealmObject(quizType: Int = 0): Record {
    val item = this
    return Record().apply {
        this.quizType = quizType
        this.day = item.date.dayOfMonth
        this.month = item.date.monthValue
        this.year = item.date.year
        this.count = item.count
        this.score = item.score
    }
}