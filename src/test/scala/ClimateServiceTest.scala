import com.github.polomarcus.utils.ClimateService
import com.github.polomarcus.model.CO2Record
import org.scalatest.funsuite.AnyFunSuite

//@See https://www.scalatest.org/scaladoc/3.1.2/org/scalatest/funsuite/AnyFunSuite.html
class ClimateServiceTest extends AnyFunSuite {
  test("containsWordGlobalWarming - non climate related words should return false") {
    assert( ClimateService.isClimateRelated("pizza") == false)
  }

  test("isClimateRelated - climate related words should return true") {
    assert(ClimateService.isClimateRelated("climate change") == true)
    assert(ClimateService.isClimateRelated("IPCC") )
  }

  //@TODO
  test("parseRawData") {
    // our inputs
    val firstRecord = (2003, 1, 355.2)     //help: to acces 2003 of this tuple, you can do firstRecord._1
    val secondRecord = (2004, 1, 375.2)
    val list1 = List(firstRecord, secondRecord)

    // our output of our method "parseRawData"
    val co2RecordWithType = CO2Record(firstRecord._1, firstRecord._2, firstRecord._3)
    val co2RecordWithType2 = CO2Record(secondRecord._1, secondRecord._2, secondRecord._3)
    val output = List(Some(co2RecordWithType), Some(co2RecordWithType2))

    // we call our function here to test our input and output
    assert(ClimateService.parseRawData(list1) == output)
  }



  //@TODO
  test("filterDecemberData") {
    val firstRecord = CO2Record(1958, 8, 313.97)
    val secondRecord = CO2Record(1959, 12, 315.72)
    val thirdRecord = CO2Record(1960, 8, 315.67)
    val fourthRecord = CO2Record(1960, 12, 316.39)

    val list = List(Some(firstRecord), Some(secondRecord), Some(thirdRecord), Some(fourthRecord))

    val expected = List(firstRecord, thirdRecord)

    assert(ClimateService.filterDecemberData(list) == expected)
  }

  test("getMinMax") {
    val firstRecord = CO2Record(1958, 6, -99.99)
    val secondRecord = CO2Record(1958, 11, 313.6)
    val thirdRecord = CO2Record(1960, 10, 314.08)
    val fourthRecord = CO2Record(1961, 4, 319.74)

    val list = List(firstRecord, secondRecord, thirdRecord, fourthRecord)

    val expected = (313.6, 319.74)

    assert(ClimateService.getMinMax(list) == expected)
  }

  test("getMinMaxByYear") {
    val firstRecord = CO2Record(2020, 11, 412.55)
    val secondRecord = CO2Record(2021, 9, 413.04)
    val thirdRecord = CO2Record(2021, 11, 414.7)
    val fourthRecord = CO2Record(2021, 12, 416.21)

    val list = List(firstRecord, secondRecord, thirdRecord, fourthRecord)

    val expected = (413.04, 416.21)


    assert(ClimateService.getMinMaxByYear(list , 2021) == expected)
  }

  test("calculateDifference") {
    val records = List(
      CO2Record(2021, 11, 414.7),
      CO2Record(2021, 12, 416.21),
      CO2Record(2022, 1, 417.37),
      CO2Record(2022, 2, 418.23),
      CO2Record(2022, 3, 419.12),
      CO2Record(2022, 4, -99.99),
    )
    val actual = ClimateService.calculateDifference(records)
    val expected = 4.42
    val tolerance = 0.001
    assert(math.abs(actual - expected) < tolerance)
  }

  test("estimateCO2LevelsFor2050") {
    val input = List(
      Some(CO2Record(2020, 11, 416.24)),
      Some(CO2Record(2021, 10, 418.23)),
      Some(CO2Record(2022, 12, 419.12))
    )
    val estimatedLevel = ClimateService.estimateCO2LevelsFor2050(input)
    val lowerBound = 420.0
    assert(estimatedLevel >= lowerBound, s"Estimated CO2 level $estimatedLevel for 2050 should be at least $lowerBound ppm")

  }
}

