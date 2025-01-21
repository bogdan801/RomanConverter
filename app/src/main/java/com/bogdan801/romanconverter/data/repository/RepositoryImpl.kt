package com.bogdan801.romanconverter.data.repository

import com.bogdan801.romanconverter.data.local_db.realm.objects.Record
import com.bogdan801.romanconverter.data.local_db.realm.objects.toRecordRealmObject
import com.bogdan801.romanconverter.domain.model.RecordItem
import com.bogdan801.romanconverter.domain.model.QuizType
import com.bogdan801.romanconverter.domain.repository.Repository
import io.realm.kotlin.Realm
import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.ext.query
import io.realm.kotlin.query.Sort
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RepositoryImpl(
    private val realm: Realm
) : Repository {
    override suspend fun saveRecord(item: RecordItem, type: QuizType) {
        realm.write {
            copyToRealm(item.toRecordRealmObject(type.ordinal), updatePolicy = UpdatePolicy.ALL)
        }
    }

    override suspend fun saveRecords(items: List<RecordItem>, type: QuizType) {
        realm.write {
            items.forEach { item ->
                copyToRealm(item.toRecordRealmObject(type.ordinal))
            }
        }

    }

    override suspend fun updateLastRecord(item: RecordItem) {
        realm.write {
            val lastRecord = this.query<Record>().sort("_id", Sort.DESCENDING).first().find()

            lastRecord?.let { record ->
                val newRecord = record.apply {
                    count = 0 + item.count
                    score = 0 + item.score
                    day = 0 + item.date.dayOfMonth
                    month = 0 + item.date.monthValue
                    year = 0 + item.date.year
                }
                copyToRealm(
                    newRecord,
                    updatePolicy = UpdatePolicy.ALL
                )
                println(newRecord)
            }
        }
    }

    override fun getRomanRecordsFlow(): Flow<List<RecordItem>> {
        return realm.query<Record>(query = "quizType == ${QuizType.GuessRoman.ordinal}")
            .asFlow()
            .map { results ->
                results.list.toList().map { it.toLeaderboardItem() }.sortedByDescending { it.score }
            }
    }

    override fun getArabicRecordsFlow(): Flow<List<RecordItem>> {
        return realm.query<Record>(query = "quizType == ${QuizType.GuessArabic.ordinal}")
            .asFlow()
            .map { results ->
                results.list.toList().map { it.toLeaderboardItem() }.sortedByDescending { it.score }
            }
    }

    override fun getBothRecordsFlow(): Flow<List<RecordItem>> {
        return realm.query<Record>(query = "quizType == ${QuizType.GuessBoth.ordinal}")
            .asFlow()
            .map { results ->
                results.list.toList().map { it.toLeaderboardItem() }.sortedByDescending { it.score }
            }
    }

    override suspend fun deleteRecord(id: Int, quizType: QuizType) {
        realm.write {
            query<Record>()
                .find()
                .forEach { record ->
                    if(record._id.hashCode() == id){
                        delete(record)
                    }
                }
        }
    }

    override suspend fun clearRecordsOfAType(quizType: QuizType) {
        realm.write {
            delete(query<Record>("quizType == $0", quizType.ordinal).find())
        }
    }
}