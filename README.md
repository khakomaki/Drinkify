# Drinkify

Android application for tracking alcohol consumption.

The estimated Blood Alcohol Content (_BAC_) is calculated in real time.

## Usage

Drinkify allows users to create, record and track consumed Drinks.

Recorded Drinks can be viewed in history.

### Create custom Drink

Created drinks can be tracked as consumed.

1. Navigate to _Drinks_
2. Press _+_
3. Fill Drink properties
4. Click _Save_

### Edit Profile

Profile settings are used to calculate the BAC.

1. Navigate to _Profile_
2. Modify any fields
3. Click _Save_

### Record consumed Drink

Recording a consumed Drink adds it to history and updates the BAC.

1. Navigate to _Record Drink_
2. Find desired Drink
3. Click _Drink_

## BAC

The BAC-meter indicates the current Blood Alcohol Content by estimating it using profile settings and recorded consumed
drinks.

> **NOTE:** estimated BAC is only indicative and should not be used to determine fitness to drive or perform other
> activities.

### Estimated by intake

following formula is used to calculate alcohol content per drink.

$$
\text{BAC} = \frac{\text{Alcohol Consumed (grams)}}{\text{Body Weight (grams)} \times r} - \text{Burn Rate} \times \text{Time (hours)}
$$

Where:

- **Alcohol Consumed (grams)** = Drink volume (mL) × Alcohol percentage × 0.78945 (ethanol density in g/mL)
- **r** = Alcohol distribution ratio (0.71 for men, 0,58 for women)
- **Burn Rate** = 0.015 (average alcohol elimination rate per hour)

### Localization

Promille (per mille, ‰) is optionally used to represent BAC in certain countries.

## Credits

Default Drink images have been provided by [flaticon](https://www.flaticon.com/).

Images:

- Beer designed by **monkik**
- Coctail designed by **Freepik**
- Jägermeister designed by **Freepik**
- Liquor designed by **Freepik**
- Whiskey designed by **Freepik**
- Wine designed by **Freepik**
