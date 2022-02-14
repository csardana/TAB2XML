package SheetMusic;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * Created by dorien on 10/11/14.
 */



public class Note {

    //we don't need an id I believe

    //type note, chord

    //private Type type;
    private Integer id;

    //careful featureX uses a letter for pitch

    private Integer measure;

    public void setMeasure(Integer measure) {
        this.measure = measure;
    }

    public Integer getMeasure() {

        return measure;
    }

    private Integer startTime;
    private Integer duration;
    private String pitch;     //Z is for rest
    private Integer voice;
    private String accidental;
    private Integer accidentalInt;
    private Integer counter;
    private Integer midiPitch;
    private Integer octave;
    private Integer staff;
    private Integer referenceNoteID;
    private Integer referenceChange;

    public void setReferenceChange(Integer referenceChange) {
        this.referenceChange = referenceChange;
    }

    public void setReferenceNoteID(Integer referenceNoteID) {
        this.referenceNoteID = referenceNoteID;
    }

    private ArrayList<Integer> durationSet = new ArrayList<Integer>();

    public Integer getReferenceNoteID() {
        return referenceNoteID;
    }

    public Integer getReferenceChange() {
        return referenceChange;
    }
    public Note(Integer id, String nonparsed) {

        this.id = id;
      //  this.type = type;
        this.staff = 1;



//


//        //fill pitchset
//        pitchSet.add(60);
//        pitchSet.add(62);
//        pitchSet.add(64);
//        pitchSet.add(65);
//        pitchSet.add(67);
//        pitchSet.add(69);
//        pitchSet.add(71);
//        pitchSet.add(72);
//        pitchSet.add(74);


            //fill duration set
            durationSet.add(2);
            durationSet.add(4);
            durationSet.add(8);


        //default values
        referenceNoteID = 0;
        referenceChange = 0;



    }

    public Note(Integer startTime) {
        this.startTime = startTime;
        this.accidental = "";
        this.midiPitch = null;
        this.accidentalInt = 0;

        this.referenceNoteID = 0;
        this.referenceChange = 0;
    }

    public void setStaff(Integer staff) {
        this.staff = staff;
    }

    public Integer getStaff() {
        if (this.staff == 1 || this.staff == 2 ){
            return staff;
        }
        else{
            System.out.println("note with no staff"+staff);
            return 0;

        }
    }

    //public Type getType() {
    //    return type;
    //}


    //types of events
   /* public enum Type {
        Chord, Note;

        public static Type parse(String s) {
            for (Type type : values()) {
                if (type.name().equals(s)) {
                    return type;
                }
            }
            throw new IllegalArgumentException("Unknown event type [" + s + "].");
        }
    }   */



    public void setRandom(){


        //TODO more efficient if sets are in fragment class

        Random random = new Random();
//        int index = random.nextInt(pitchSet.size());

//        this.pitch = pitchSet.get(index);

        int index = random.nextInt(durationSet.size());

        this.duration = durationSet.get(index);


    }

//    public Integer getPitch() {
//        return pitch;
//    }

    public Integer getDuration() {
        return duration;
    }

//    public ArrayList<Integer> getPitchSet() {
//        return pitchSet;
//    }

    public ArrayList<Integer> getDurationSet() {
        return durationSet;
    }

//    public void setPitch(Integer pitch) {
//        this.pitch = pitch;
//    }

//    public void changeRandom(){
//        Random random = new Random();
//        this.pitch =  random.nextInt(pitchSet.size());
//    }

//    public Note() {
//
//
//    }

    public void setId(int id) {
        this.id = id;
    }


    public void setCounter(Integer counter) {
        this.counter = counter;
    }

    public Integer getCounter() {
        return counter;
    }

    public Integer getVoice() {
        return voice;
    }

    public void setVoice(Integer voice) {
        this.voice = voice;
    }

    public void setStartTime(Integer startTime) {
        this.startTime = startTime;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public void setPitch(String pitch) {
        this.pitch = pitch;
    }

    public Integer getStartTime() {
        return startTime;
    }

    public String getPitch() {
        return pitch;
    }

    public void setAccidentalInt(Integer acc){
        this.accidentalInt = acc;
    }


    public void setAccidental(String accidental) {
        this.accidental =  accidental;
    }

    public String getAccidental() {

        return accidental;
    }

    public Integer getMidiPitch() {
        return midiPitch;
    }

    public void setMidiPitch(Integer midiPitch) {
        this.midiPitch = midiPitch;
    }

    public Integer getOctave() {
        return octave;
    }

    public void setOctave(Integer octave) {
        this.octave = octave;
    }

    public void calculateMidiPitch() {
        //todo

        Integer baseMidi = 0;
        if (this.pitch.equals("C")){
            baseMidi = 0;
        }
        else if(this.pitch.equals("D")){
            baseMidi = 2;

        }else if(this.pitch.equals("E")){
            baseMidi = 4;
        }else if(this.pitch.equals("F")){
            baseMidi = 5;
        }else if(this.pitch.equals("G")){
            baseMidi = 7;
        }else if(this.pitch.equals("A")){
            baseMidi = 9;
        }else if(this.pitch.equals("B")) {
            baseMidi = 11;
        }

        if (!this.pitch.equals("Z")){

            baseMidi = baseMidi + this.accidentalInt;


            this.midiPitch = baseMidi + ( 12 * (this.octave + 1));

            int test = 0;
        }


    }



    public Integer getId() {
        return id;
    }








}
