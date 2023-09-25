# Meeting Planner

Meeting Planner is a Java program designed to facilitate the planning of meetings and the collection of preferences for meeting slots. This Object-Oriented Programming project is structured into various classes located in the `it.polito.meet` package, with the main class being `MeetServer`. The usage examples and required checks for the program can be found in the `TestApp` class in the example package.

## Usage

To use Meeting Planner, adhere to the following guidelines:

### R1: Categories and Meetings

- **addCategories(categories: List<String>)**: This method allows you to add meeting categories to the list of available categories.

- **getCategories()**: Retrieve the list of available categories.

- **addMeeting(title: String, topic: String, category: String) -> meetingID: int**: Add a new meeting with a specified category. This method returns a unique ID for the meeting. If the specified category does not exist, it throws a `MeetException`.

- **getMeetings(category: String) -> List<int>**: Retrieve a list of meetings that belong to a given category.

- **getMeetingTitle(meetingID: int) -> String**: Retrieve the title of a meeting based on its ID.

- **getMeetingTopic(meetingID: int) -> String**: Retrieve the subject of a meeting based on its ID.

### R2: Meeting Slot Options

Meeting options and slot information can be managed using the following methods:

- **addOption(meetingID: int, date: String, startTime: String, endTime: String) -> slotLength: int**: Add a new option slot for a meeting. This method ensures that the new slot does not overlap with any existing slots for the same meeting. If the provided meeting ID is invalid, a `MeetException` is thrown. The method calculates the length of the slot in hours and returns this value.

- **showSlots(meetingID: int) -> Map<String, List<String>>**: Retrieve information about available slots for a specific meeting. The returned value is a map where each date is a key, and the corresponding value is a list of slots described in the format "hh:mm-hh:mm".

### R3: Preferences

Manage preferences for meetings using the following methods:

- **openPoll(meetingID: int)**: Declare a meeting open for collecting preferences.

- **selectPreference(email: String, name: String, surname: String, meetingID: int, date: String, timeRange: String) -> totalPreferences: int**: Record preferences for a specific slot or option of a meeting. Preferences can only be recorded for meetings that have an opened poll. The method adds the preference and returns the total number of preferences recorded for that slot. If an invalid meeting ID or slot is provided, a `MeetException` is thrown.

- **listPreferences(meetingID: int) -> List<String>**: Retrieve the list of preferences expressed for a specific meeting. Each preference is represented as a string in the format "YYYY-MM-DDThh:mm-hh:mm=EMAIL".

### R4: Close Poll

Close the poll associated with a meeting and determine the most preferred options using the following method:

- **closePoll(meetingID: int) -> List<String>**: Close the poll associated with a meeting. The method retrieves the most preferred options in the format "YYYY-MM-DDThh:mm-hh:mm=##".

### R5: Stats

Methods for analyzing meeting preferences and participation:

- **meetingPreferences(meetingID: int) -> Map<String, List<String>>**: Retrieve the preference count for each slot of a meeting. This method provides an overview of the preferences expressed for each slot within the meeting.

- **preferenceCount() -> Map<int, int>**: Calculate the total number of preferences collected for each meeting. This method allows for assessing the level of interest or popularity of each meeting based on the number of preferences received.

For more details on how to use these methods and their exceptions, refer to the provided example in the `TestApp` class.
