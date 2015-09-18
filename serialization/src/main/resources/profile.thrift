
namespace java com.senacor.hackingdays.serialization.thirftdata

enum Gender {
    Male, Female, Disambiguous
}

enum RelationShipStatus {
    Divorced, Maried, Single
}

struct Profile {
    1: string name,
    2: Gender gender,
    3: i32 age,
    4: Location location,
    5: RelationShipStatus relationShip,
    6: bool smoker,
    7: Seeking seeking,
    8: Activity activity,
}

struct Activity {

    1: i64 lastLoginTimestamp,
    2: i32 loginCount,
}

struct Location {

    1: string state,
    2: string city,
    3: string zip,
}

struct Range {
    1: i32 lower,
    2: i32 upper,
}

struct Seeking {
    1: Gender gender,
    2: Range ageRange
}






