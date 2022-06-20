package mobile.stream

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types._

object AppEventStream
{
  val spark = SparkSession.builder()
    .appName("MobileAppEventAnalysis")
    .master("local[*]")
    .getOrCreate()

  val dataSchema = new StructType()
    .add("ID",StringType,false)
    .add("ACTION", StringType, false)
    .add("TS", TimestampType, false)

  val AppEventDF = spark.readStream
    .schema(dataSchema)
    .json("")

  val AppEventWD = AppEventDF.groupBy(window(col("TS"),"10 minutes")).count()

  AppEventWD.writeStream
    .format("console")
    .outputMode("complete")
    .option("truncate", "false")
    .start()
    .awaitTermination()

}
