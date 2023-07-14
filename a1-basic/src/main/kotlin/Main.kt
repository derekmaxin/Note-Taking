
import javafx.application.Application
import javafx.event.EventHandler
import javafx.geometry.Insets
import javafx.geometry.Orientation
import javafx.scene.Node
import javafx.scene.Scene
import javafx.scene.control.*
import javafx.scene.layout.*
import javafx.scene.paint.Color
import javafx.stage.Stage

class Main : Application()  {
    override fun start(stage: Stage) {
        stage.title = "CS349 - A1 Notes - dmaxin"
        stage.minWidth = 640.0
        stage.minHeight = 480.0
        val root = BorderPane()

        var totalNotes : Int = 4
        var activeNotes : Int = 3
        var isListView = true;


        // TOOLBAR
        val viewLabel = Label("View:").apply{
            padding = Insets(0.0, 10.0, 0.0, 10.0)
        }
        val listButton = Button("List").apply {
            prefWidth = 50.0
        }
        val listButtonDisabled = Button("List").apply {
            prefWidth = 50.0
            isDisable = true;
        }
        val padder1 = Pane().apply {
            maxWidth = 5.0
            minWidth = 5.0
        }
        val gridButton = Button("Grid").apply {
            prefWidth = 50.0
        }
        val gridButtonDisabled = Button("Grid").apply {
            prefWidth = 50.0
            isDisable = true
        }
        val separator1 = Separator().apply{
            padding = Insets(0.0, 5.0, 0.0, 5.0)
        }
        val showArchivedLabel = Label("Show archived:")
        val archivedCheckbox = CheckBox().apply{
            padding = Insets(0.0, 5.0, 0.0, 5.0)
        }
        val separator2 = Separator()
        val orderByLabel = Label("Order by:").apply{
            padding = Insets(0.0, 5.0, 0.0, 5.0)
        }
        val comboBox: ComboBox<String> = ComboBox()
        comboBox.items.addAll(
            "Length (asc)", "Length (desc)"
        )
        comboBox.promptText = "Length (asc)"
        val rightSpacer = Pane()
        HBox.setHgrow(
            rightSpacer,
            Priority.ALWAYS,
        )
        val clearButton = Button("Clear").apply {
            prefWidth = 50.0
        }

        fun topToolbarVbox(newIsListView : Boolean) : Node {
            if (newIsListView) {
                return VBox(ToolBar(
                    viewLabel, listButtonDisabled, padder1, gridButton, separator1, showArchivedLabel, archivedCheckbox,
                    separator2, orderByLabel, comboBox, rightSpacer, clearButton
                ).apply {
                    padding = Insets(10.0, 10.0, 10.0, 0.0)
                })
            } else {
                return VBox(ToolBar(
                    viewLabel, listButton, padder1, gridButtonDisabled, separator1, showArchivedLabel, archivedCheckbox,
                    separator2, orderByLabel, comboBox, rightSpacer, clearButton
                ).apply {
                    padding = Insets(10.0, 10.0, 10.0, 0.0)
                })
            }
        }
        // END OF TOOLBAR

        // STATUS BAR

        fun statusBarText() : String {
            var totalNotesText = ""
            var activeNotesText = ""
            if (totalNotes == 1) totalNotesText = "1 note, "
            else totalNotesText = "$totalNotes notes, "
            if (activeNotes == 1) activeNotesText = "1 of which is active"
            else activeNotesText = "$activeNotes of which are active"
            return totalNotesText + activeNotesText
        }
        fun createStatusBar() : Label {
            return Label(statusBarText()).apply {
                border = Border(
                    BorderStroke(
                        Color.BLACK,
                        BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths(1.0, 0.0, 0.0, 0.0)
                    )
                )
                prefWidth = Double.MAX_VALUE
            }
        }
        // END OF STATUS BAR

        // NOTES SECTION
        var isAscending : Boolean = true
        var showArchived : Boolean = false

        //PREMADE NOTES
        var myNote = note()
        myNote.text = "Baby note"
        myNote.isArchived = false

        var myNote2 = note()
        myNote2.text = "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. " +
                "Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. " +
                "Donec quam felis, ultricies nec, pellentesque eu, pretium quis, sem. Nulla consequat massa quis enim. " +
                "Donec pede justo and even more text to make this a little longer"
        myNote2.isArchived = false

        var myNote3 = note()
        myNote3.text = "Hello I was hiding"
        myNote3.isArchived = true

        var myNote4 = note()
        myNote4.text = "I think someone is hiding around here"
        myNote4.isArchived = false

        //Array of notes holds the properties of each note, not the HBox themselves
        var arrayofNotes = arrayOf<note>(myNote, myNote3, myNote4, myNote2)


        val createNoteTextField = TextField().apply{
            HBox.setHgrow(this, Priority.ALWAYS)
            prefHeight = 42.0
        }
        val createNoteButton = Button("Create").apply{
            prefWidth = 75.0
            prefHeight = 42.0
        }
        val createNoteTextFieldGrid = TextArea().apply{
            VBox.setVgrow(this, Priority.ALWAYS)
            isWrapText = true
            prefHeight = 163.0
        }
        val createNoteButtonGrid = Button("Create").apply{
            prefWidth = 205.0
            prefHeight = 32.0
        }

        var createNote = HBox(createNoteTextField, createNoteButton).apply{
            background = Background(BackgroundFill(Color.LIGHTSALMON, CornerRadii(10.0), null))
            spacing = 10.0
            padding = Insets(10.0,10.0,10.0,10.0)
        }
        var createNoteGrid = VBox(createNoteTextFieldGrid, createNoteButtonGrid).apply{
            background = Background(BackgroundFill(Color.LIGHTSALMON, CornerRadii(10.0), null))
            spacing = 10.0
            padding = Insets(10.0,10.0,10.0,10.0)
            VBox.setVgrow(this, Priority.ALWAYS)
        }

        fun createListNotesRoot() : Node {
            if (isListView) {
                //LIST VIEW
                return ScrollPane(VBox(createNote).apply {
                    //add all preset notes to the UI
                    (arrayofNotes).forEach {
                        val cb = CheckBox("Archived").apply {
                            isSelected = it.isArchived
                            HBox.setHgrow(this, Priority.NEVER)
                            minWidth = 70.0
                        }
                        var itHolder = it // 'it' needs to referenced as the note in the eventhandler as well
                        cb.onAction = EventHandler {
                            itHolder.isArchived = cb.isSelected
                            if (itHolder.isArchived) activeNotes-- else activeNotes++
                            root.center = createListNotesRoot() //refesh
                            root.bottom = createStatusBar()
                        }
                        if (!it.isArchived || showArchived)
                            children.add(HBox(
                                Label(it.text).apply {
                                    HBox.setHgrow(this, Priority.ALWAYS)
                                    VBox.setVgrow(this, Priority.ALWAYS)
                                    isWrapText = true
                                },
                                Pane().apply {
                                    HBox.setHgrow(
                                        this, Priority.SOMETIMES,
                                    )
                                },
                                cb,
                            ).apply {
                                background = Background(
                                    BackgroundFill(
                                        if (!it.isArchived) Color.LIGHTYELLOW else Color.LIGHTGRAY,
                                        CornerRadii(10.0),
                                        null
                                    )
                                )
                                //prefHeight = 40.0
                                maxWidth = Double.MAX_VALUE
                                //maxHeight = 40.0
                                spacing = 10.0
                                padding = Insets(10.0, 10.0, 10.0, 10.0)
                            })
                    }
                    padding = Insets(10.0, 10.0, 10.0, 10.0)
                    spacing = 10.0
                }).apply{
                    this.isFitToWidth = true
                }
            }else{
                //GRID VIEW
                return ScrollPane(TilePane(Orientation.HORIZONTAL).apply{
                    //add all preset notes to the UI
                    children.add(VBox(createNoteGrid).apply{
                        prefHeight = 225.0
                        prefWidth = 225.0
                        maxWidth = 225.0
                        maxHeight = 225.0
                    })
                    (arrayofNotes).forEach {
                        val cb = CheckBox("Archived").apply {
                            isSelected = it.isArchived
                            VBox.setVgrow(this, Priority.NEVER)
                        }
                        var itHolder = it // 'it' needs to referenced as the note in the eventhandler as well
                        cb.onAction = EventHandler {
                            itHolder.isArchived = cb.isSelected
                            if (itHolder.isArchived) activeNotes-- else activeNotes++
                            root.center = createListNotesRoot() //refesh
                            root.bottom = createStatusBar()
                        }
                        if (!it.isArchived || showArchived)
                            children.add(VBox(
                                Label(it.text).apply {
                                    VBox.setVgrow(this, Priority.ALWAYS)
                                    isWrapText = true
                                },
                                Pane().apply {
                                    VBox.setVgrow(
                                        this, Priority.SOMETIMES,
                                    )
                                },
                                cb,
                            ).apply {
                                background = Background(
                                    BackgroundFill(
                                        if (!it.isArchived) Color.LIGHTYELLOW else Color.LIGHTGRAY,
                                        CornerRadii(10.0),
                                        null
                                    )
                                )
                                prefHeight = 225.0
                                prefWidth = 225.0
                                maxWidth = 225.0
                                maxHeight = 225.0
                                spacing = 10.0
                                padding = Insets(10.0, 10.0, 10.0, 10.0)
                            })
                    }
                    padding = Insets(10.0, 10.0, 10.0, 10.0)
                    this.hgap = 20.0
                    this.vgap = 20.0
                }).apply{
                    this.isFitToWidth = true
                }
            }
        }

        //creating new notes
        createNoteButton.onAction = EventHandler {
            var newNote = note();
            newNote.text = createNoteTextField.text
            newNote.isArchived = false;
            createNoteTextField.text = ""
            arrayofNotes = arrayofNotes.plus(newNote)
            totalNotes++
            activeNotes++

            arrayofNotes = sortByTextLength(arrayofNotes, isAscending)

            root.center = createListNotesRoot()
            root.bottom = createStatusBar()
        }
        createNoteButtonGrid.onAction = EventHandler {
            var newNote = note();
            newNote.text = createNoteTextFieldGrid.text
            newNote.isArchived = false;
            createNoteTextFieldGrid.text = ""
            arrayofNotes = arrayofNotes.plus(newNote)
            totalNotes++
            activeNotes++

            arrayofNotes = sortByTextLength(arrayofNotes, isAscending)

            root.center = createListNotesRoot()
            root.bottom = createStatusBar()
        }

        // asc/desc
        comboBox.onAction = EventHandler {
            if (comboBox.value == "Length (asc)") isAscending = true
            else isAscending = false
            arrayofNotes = sortByTextLength(arrayofNotes, isAscending)
            root.center = createListNotesRoot()
        }

        //show archived
        archivedCheckbox.onAction = EventHandler {
            showArchived = archivedCheckbox.isSelected
            root.center = createListNotesRoot()
        }

        //list/grid view
        listButton.onAction = EventHandler {
            isListView = true;
            root.center = createListNotesRoot()
            root.top = topToolbarVbox(true)
        }

        gridButton.onAction = EventHandler {
            isListView = false;
            root.center = createListNotesRoot()
            root.top = topToolbarVbox(false)
        }

        //clear
        clearButton.onAction = EventHandler {
            arrayofNotes = arrayOf<note>()
            totalNotes = 0;
            activeNotes = 0;
            root.center = createListNotesRoot()
            root.bottom = createStatusBar()
        }

        //END OF NOTES SECTION

        //TOP LEVEL BORDERPANE

        root.top = topToolbarVbox(true)
        root.center = createListNotesRoot()
        root.bottom = createStatusBar()
        stage.scene = Scene(root, 800.0, 600.0)

        stage.show()
    }

    class note {
        var text : String = ""
        var isArchived : Boolean = false
    }

    fun sortByTextLength(notes : Array<note>, isAscending : Boolean) : Array<note> {
        var sortedArray : Array<note> = arrayOf<note>()

        if (isAscending) sortedArray = notes.sortedWith(compareBy{it.text.length}).toTypedArray() //ascending
        else sortedArray = notes.sortedWith(compareBy{it.text.length}).reversed().toTypedArray() //descending

        return sortedArray
    }
}