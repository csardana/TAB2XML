//package SheetMusic;
//
//
//
//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Element;
//import org.jsoup.parser.Parser;
//
//import java.io.*;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.HashMap;
//import java.util.Map;
//
//
//
//
///**
// * Created by Dorien Herremans on 02/02/15.
// */
//public class ParseIntoSheetMusic {
//
//    public org.jsoup.nodes.Document doc;
//    HashMap<Integer, Integer> divMultiplier;
//    private ArrayList<Note> notesOfSong;  //parsed all notes in order of file
//    private ArrayList<Integer> rests;
//    private String[] flatSong, flatSongWithRests;
//    private ArrayList<ArrayList<Note>> songMatrix;
//    public ArrayList<Note> notesOfSongNoRest;
//    public int divisions;
//
//
//    public ParseIntoSheetMusic(String musicXml) throws IOException {
//
////        File input = new File(filename);
////        "", Parser.xmlParser()
//
////        String xml = Jsoup.connect("http://localhost:8080/HTTP_Connection/index.php")
////                .get().toString();
////        Document doc = Jsoup.parse(xml, "", Parser.xmlParser());
//
//
//        String xml = musicXml;
//        FileWriter myWriter = new FileWriter("musicXmlFile.txt");
//        myWriter.write(xml);
//        myWriter.close();
//        InputStream is = new FileInputStream("musicXmlFile.txt") {
//            @Override
//            public int read() throws IOException {
//                return 0;
//            }
//        };
//
//          doc = Jsoup.parse(is, "UTF-8", "", Parser.xmlParser());
//
////        doc = Jsoup.parse(input, "UTF-8", filename);
//
//        //check if characterset is ok
//
////        OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(input), "UTF-8");
//
//        if (doc.getElementsByTag("note").isEmpty()) {
//            doc = Jsoup.parse(is, "UTF-8", "", Parser.xmlParser());
////            doc = Jsoup.parse(input, "UTF-16", filename);
//            if (doc.getElementsByTag("note").isEmpty()) {
//                System.out.println("Please check that your file is encoded in UTF-8 or UTF-16 and contains notes.");
//            }
//        }
//
//
//    }
//
//
//    public String[] parseMusicXML() {
//        //read in a MusicXNL file
//
//
//        //check part-list for divisions
//        //for each part
//        //for each staff and voice
//        //rests
//
//
//        ArrayList<Integer> voicesIndex = new ArrayList<Integer>();    //list of unique voices
//        ArrayList<String> song = new ArrayList<String>();
//        notesOfSong = new ArrayList<Note>();
//
//        String[] flatSong = null;
//
//
//        //get the number of divisions and the change throughout the piece
//
//        ArrayList<Integer> divisions = new ArrayList();
//
//        for (Element thisdiv : this.doc.getElementsByTag("divisions")) {
//            divisions.add(Integer.valueOf(thisdiv.text()));
//        }
//        divMultiplier = new HashMap<Integer, Integer>();
//        //System.out.print(this.doc.text());
//
//        if (!this.doc.getElementsByTag("divisions").isEmpty()) {
//
//
//            //System.out.println("divisions; " + lcm(divisions));
//            Integer lcm = lcm(divisions);
//
//            //set the multiplier for each division.
//            for (Integer i : divisions) {
//                divMultiplier.put(i, (int) lcm / i);
//            }
//        } else {
//
//            divMultiplier.put(1, 1);
//        }
//        //voicesIndex = getNumberOfVoices();
//
//        notesOfSong = getAllNotes();
//
//
//        flatSong = setSongArrayOfStrings(notesOfSong);
//
//        songMatrix = setSongMatrix(notesOfSong);
//
//
//        //set ID for each note
//        for (int i = 1; i <= notesOfSong.size(); i++){
//            notesOfSong.get(i-1).setId(i);
//        }
//
//
//        removeRests();
//
//
//
//        return flatSong;
//
//    }
//
//
//    public void removeRests(){
//
//        this.notesOfSongNoRest = new ArrayList<>();
//
//        for (int i = 0; i < notesOfSong.size(); i++){
//            if (notesOfSong.get(i).getPitch() != "Z") {
//
//
//                this.notesOfSongNoRest.add(notesOfSong.get(i));
//            }
//
//
//        }
//    }
//
//    public ArrayList<ArrayList<Note>> getSongMatrix() {
//        return songMatrix;
//    }
//
//    public ArrayList<Note> getNotesOfSongNoRests() {
//        return this.notesOfSongNoRest;
//    }
//
//    public ArrayList<Note> getNotesOfSong() {
//        return notesOfSong;
//    }
//
//
//
//    private String[] setSongArrayOfStrings(ArrayList<Note> notesOfSong) {
//
//        String[] flatSong;
//        //find total duration
//        Integer numberOfSlices = 0;
//        for (Note inote : notesOfSong) {
//            if (inote.getDuration() + inote.getStartTime() > numberOfSlices) {
//                numberOfSlices = inote.getDuration() + inote.getStartTime();
//            }
//            //System.out.print("starters: " + inote.getStartTime() + " ");
//        }
////        System.out.print("slices: " + numberOfSlices);
//
//
//        //array in elaine's string format
//
//        flatSong = new String[numberOfSlices];
//        for (Note inote : notesOfSong) {
//            //System.out.print(inote.getDuration() );
//            if (inote.getPitch() != "Z") {
//                for (int i = 0; i < inote.getDuration(); i++) {
//                    if (flatSong[i + inote.getStartTime()] == null) {
//                        flatSong[i + inote.getStartTime()] = "";
//                    } else {
//                        flatSong[i + inote.getStartTime()] += " ";
//
//                    }
//
//                    flatSong[i + inote.getStartTime()] += inote.getPitch() + inote.getAccidental();
//                }
//            }
//        }
//
//
//        //remove lines with only rests
//        flatSongWithRests = flatSong.clone();
//        rests = new ArrayList<Integer>();
//
//        int count = 0;
//        for (int i = 0; i < flatSong.length; i++) {
//            if (flatSong[i] == null) {
//
//                //System.out.print("i: "+ i + flatSong.length);
//                rests.add(i);
//                count++;
//            }
//        }
//
////        System.out.println("full rests: " + count + "end");
////        System.out.println("length: " + flatSong.length + "end");
//        String[] newFlatSong = new String[flatSong.length - count];
//
//        count = 0;
//        for (int i = 0; i < flatSong.length; i++) {
//            if (flatSong[i] == null) {
//                count++;
//            } else {
//                newFlatSong[i - count] = flatSong[i];
//
//            }
//        }
//
//        //System.out.print("test"+flatSong.length) ;
//
//        return newFlatSong;
//    }
//
//
//    public String[] getFlatSongWithRests() {
//        return flatSongWithRests;
//    }
//
//    private ArrayList<ArrayList<Note>> setSongMatrix(ArrayList<Note> notesOfSong){
//        //list of note ideas per slice
//
//
//        //todo
//        Integer numberOfSlices = 0;
//        for (Note inote : notesOfSong) {
//            if (inote.getDuration() + inote.getStartTime() > numberOfSlices) {
//                numberOfSlices = inote.getDuration() + inote.getStartTime();
//            }
//            //System.out.print("starters: " + inote.getStartTime() + " ");
//        }
//
//
//        ArrayList<ArrayList<Note>> songMatrix = new ArrayList<>();
//        //array in elaine's string format
//        //put an arraylist in each slice
//
//        for (int i = 0; i < numberOfSlices; i++){
//
//            ArrayList<Note> mylist = new ArrayList<Note>();
//            songMatrix.add(mylist);
//
//        }
//
//        //String[] PitchesSong;
//        //PitchesSong = new String[numberOfSlices];
//        for (Note inote : notesOfSong) {
//            //System.out.print(inote.getDuration() );
//            if (inote.getPitch() != "Z") {
//                for (int i = 0; i < inote.getDuration(); i++) {
//
//                    songMatrix.get(i + inote.getStartTime()).add(inote);
//                }
//            }
//        }
//
//
//
//
//
//        return songMatrix;
//
//    }
//
//
//    private String[] setSongArrayOfPitches(ArrayList<Note> notesOfSong) {
//
//        //todo no rests in here. What with rest
//
//        String[] PitchesSong;
//        //find total duration
//        Integer numberOfSlices = 0;
//        for (Note inote : notesOfSong) {
//            if (inote.getDuration() + inote.getStartTime() > numberOfSlices) {
//                numberOfSlices = inote.getDuration() + inote.getStartTime();
//            }
//            //System.out.print("starters: " + inote.getStartTime() + " ");
//        }
////        System.out.print("slices: " + numberOfSlices);
//
//
//        //array in elaine's string format
//
//        PitchesSong = new String[numberOfSlices];
//        for (Note inote : notesOfSong) {
//            //System.out.print(inote.getDuration() );
//            if (inote.getPitch() != "Z") {
//                for (int i = 0; i < inote.getDuration(); i++) {
//                    if (PitchesSong[i + inote.getStartTime()] == null) {
//                        PitchesSong[i + inote.getStartTime()] = "";
//                    } else {
//                        PitchesSong[i + inote.getStartTime()] += " ";
//
//                    }
//
//                    PitchesSong[i + inote.getStartTime()] += inote.getMidiPitch() + inote.getAccidental();
//                }
//            }
//        }
//
//
//        //remove lines with only rests
//        flatSongWithRests = PitchesSong;
//        rests = new ArrayList<Integer>();
//
//        int count = 0;
//        for (int i = 0; i < PitchesSong.length; i++) {
//            if (PitchesSong[i] == null) {
//
//                //System.out.print("i: "+ i + flatSong.length);
//                rests.add(i);
//                count++;
//            }
//        }
//
////        System.out.println("full rests: " + count + "end");
////        System.out.println("length: " + PitchesSong.length + "end");
//        String[] newFlatSong = new String[PitchesSong.length - count];
//
//        count = 0;
//        for (int i = 0; i < PitchesSong.length; i++) {
//            if (PitchesSong[i] == null) {
//                count++;
//            } else {
//                newFlatSong[i - count] = PitchesSong[i];
//
//            }
//        }
//
//        //System.out.print("test"+flatSong.length) ;
//
//        return newFlatSong;
//    }
//
//
//    public void writeXMLSegmented(HashMap<Integer, String> segmentKeys) throws IOException {
//
//
////        for (Note inote : notesOfSong) {
////
////            if (segmentKeys.containsKey(inote.getStartTime())){
////
////
////
////            //System.out.print("starters: " + inote.getStartTime() + " ");
////        }
////    }
//
////
////        for (Element note : this.doc.getElementsByTag("Note")) {
////
//////                System.out.print("voice: "+ note.getElementsByTag("voice").text());
////            //voices.add(Integer.valueOf(note.getElementsByTag("voice").text()));
////
////            //if not rest    -> pitch is not empty
////            if (segmentKeys.containsKey(count)){
////
////            }
////            //count++
////
////
////        }
//
//        int count = 0;
//        for (int t = 0; t < flatSongWithRests.length; t++) {
////            System.out.println("t: " + t);
//            if (flatSongWithRests[t] == null) {
//                //rest
//
//                for (Map.Entry<Integer, String> key : segmentKeys.entrySet()) {
//                    //for each segment
//                    //add rest
//
//                    if (key.getKey() > t) {
//                        segmentKeys.put(key.getKey(), segmentKeys.get(key.getKey()) + 1);
//
//                    }
//
//
//                }
//                count++;
//            }
//        }
//
//        setKeysOnNotes(segmentKeys);
//
////        for (Map.Entry<Integer, String> map1 :  segmentKeys.entrySet()){
////            System.out.print(""+ map1.getValue());
////        }
////
////            }
////        }
//
////                String html = "<root><child></child><child></chidl></root>";
////                Document doc = Jsoup.parse(html);
//
//        //have list with rests
//        //write the key for each segment
//        //get list of key + segment start
//        //output:
////        inside the note object
////        <lyric>
////        <syllabic>single</syllabic>
////        <text>nicht,</text>
////        </lyric>
//
//
//
//        //System.out.println(doc.data().toString());
//
//        //doc.select("score-partwise").first().children().after("<note><text>label</text></part>");
////                        first().children().first().before("<newChild></newChild>");
//        //System.out.println(doc.body().html());
//        doc.outputSettings().indentAmount(0).prettyPrint(false);  //escapeMode(Entities.EscapeMode.xhtml)
//
//
//        BufferedWriter htmlWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("output.xml"), "UTF-8"));
//        htmlWriter.write(doc.toString());           //.replaceAll(">\\s+",">").replaceAll("\\s+<","<")
//
//        htmlWriter.close();
//    }
//
//    private ArrayList<Integer> getNumberOfVoices() {
//        //detect number of voices
//
//        ArrayList<Integer> voices = new ArrayList<Integer>();
//        ArrayList<Integer> voicesIndex = new ArrayList<Integer>();    //list of unique voices
//
//        voices.clear();
//
//        for (Element note : this.doc.getElementsByTag("Note")) {
//
////                System.out.print("voice: "+ note.getElementsByTag("voice").text());
//            voices.add(Integer.valueOf(note.getElementsByTag("voice").text()));
//
//        }
//
//        //System.out.print("size: " + voices.size());
//
//        //select only unique voices
//
//        Collections.sort(voices);
//        voicesIndex.clear();
//        voicesIndex.add(voices.get(0));
//        if (voices.size() > 1) {
//            for (int i = 1; i < voices.size(); i++) {
//                if (voices.get(i) != voices.get(i - 1)) {
//                    voicesIndex.add(voices.get(i));
//                }
//            }
//        }
//
//        return voicesIndex;
//    }
//
//
//    public ArrayList<Note> getAllNotes() {
//
//
//        ArrayList<Note> notesOfSong = new ArrayList<Note>();
//        Integer currentVoice = 0;
//
//        Integer duration;
//        //HashMap<Integer, Integer> positionOfVoice = new HashMap();
//        //HashMap<Integer, Integer> lastDurationsOfVoices = new HashMap();
//
//        notesOfSong.clear();
//        //positionOfVoice.clear();
//
//        Integer position = 0;
//        Integer lastDuration = 0;
//        Integer counter = 0;
//
//
////        //keep the last duration for each voice
////        for (Integer voice : voicesIndex) {
////            lastDurationsOfVoices.put(voice, 0);
////        }
////
////        //keep a start duration for each voice
////        for (Integer voice : voicesIndex) {
////            positionOfVoice.put(voice, 0);
////        }
//
//
//
//        for (int z = 0; z < this.doc.select("part").size(); z++ ){
////        for (Element thisPart : this.doc.select("part")) {
//
//            position = 0;
//            lastDuration = 0;
//            counter = 1;
//
//            divisions = 1;
//
//            //get all notes
////        for (Element thiselement : thisPart.children()) {
////        for (Element thisnote : thisPart.getElementsByTag("Note")) {
//            //go through all the elements, and only if note and forward backward
//
//            for (Element thismeasure : this.doc.select("part").get(z).getElementsByTag("measure")) {
//
//                String measure = "0";
//
//                if (!thismeasure.getElementsByTag("divisions").isEmpty()) {
//                    divisions = Integer.valueOf(thismeasure.getElementsByTag("divisions").text());
//                }
//                measure = thismeasure.attr("number");
//
//                for (Element thisnote : thismeasure.children()) {
////                thiselement.getElementsByTag("Note")
//
//
////                System.out.println(thisnote.tagName());
//                    if (thisnote.tagName().equals("note")) {
//
//                        Note note = new Note(0);
//                        counter++;
//
//
//                        //System.out.println("\n note:");
//
//                        //System.out.print("DIVISIONS: " + divisions);
//
//
//                        //get current voice
//                        currentVoice = Integer.valueOf(thisnote.getElementsByTag("voice").text());
//                        note.setVoice(currentVoice);
//
//
//
//                        //get the pitch
//                        if (!thisnote.getElementsByTag("pitch").isEmpty()) {
//                            for (Element thispitch : thisnote.getElementsByTag("pitch")) {
//                                //                        System.out.print(thispitch.getElementsByTag("step").text());
//                                note.setPitch(thispitch.getElementsByTag("step").text());
//                                String octave = thispitch.getElementsByTag("octave").text().replaceAll("^\\s+|\\s+$|\\s*(\n)\\s*|(\\s)\\s*", "$1$2");
//                                Integer octaveInt = Integer.parseInt(octave);
//                                note.setOctave(octaveInt);
//
//                                //System.out.print("alter: " + thispitch.getElementsByTag("alter").text());
//                                String alter = String.valueOf(thispitch.getElementsByTag("alter").text());
//                                if (!thispitch.getElementsByTag("alter").isEmpty()) {
//
//                                    note.setAccidentalInt(Integer.parseInt(alter));
//
//                                    if (alter.equals("1")) {
//                                        note.setAccidental("#");
//                                    } else if (alter.equals("-1")) {
//                                        note.setAccidental("b");
//                                    } else if (alter.equals("2")) {
//                                        note.setAccidental("##");
//                                    } else if (alter.equals("-2")) {
//                                        note.setAccidental("bb");
//                                    }
//                                }
//                                note.calculateMidiPitch();
//                            }
//
//                        } else { //if (note.getElementsByTag("rest")!= null){
//                            //getting other elements like rests is important for determining start onsets of the next note
//                            //                    System.out.println(note.getElementsByTag("rest").text());
//
//                            note.setPitch("Z");
//
//                        }
//
//
//
//                        //System.out.print("\n" + note.getPitch() + " ");
//                        //                System.out.print(" for " +  thisnote.getElementsByTag("duration").text() + ", ");
//
//                        //System.out.println(thisnote.text()+"ok"+ divisions);
//
//                        if (thisnote.getElementsByTag("duration").text().isEmpty()) {
//                            duration = 0;     //todo ornamentals
//                        } else {
//                            duration = Integer.valueOf(thisnote.getElementsByTag("duration").text()) * divMultiplier.get(divisions);
//
//                        }
//
//                        note.setDuration(duration);
//
//
//                        int staff;
//
//                        if (thisnote.getElementsByTag("staff").text().isEmpty()){
//                            staff=currentVoice; //todo tell the user they need toc heck this.
//
//                            //System.out.println("no staff specified for an element in the xml, measure " + measure +
//                            //        " voice: " + currentVoice);
//                            //System.out.println(thisnote.getElementsByTag("staff").text() +" -");
//
//
//                        }
//                        else {
//                            staff = Integer.valueOf(thisnote.getElementsByTag("staff").text());
//                        }
//
//                        note.setStaff(staff);
//
//
//
//                        //System.out.print(" dur: " + duration);
//                        //System.out.print("voice: " + currentVoice);
//
//                        //now check if it is a chord
//                        if (!thisnote.getElementsByTag("chord").isEmpty()) {
//
//
//                            note.setStartTime(position);
//
//
//                            //retract previous duration
//                            note.setStartTime(position - lastDuration);
//                            //System.out.print(" start: " + note.getStartTime());
//
//                            //System.out.print("chord");
//
//
//                        } else {
//                            //increment start time of the current voice
//                            //System.out.print(" start: " + position);
//                            note.setStartTime(position);
//                            position = position + duration;
//
//
//                        }
//
//                        lastDuration = duration;
//                        note.setCounter(counter);
//
////                        if (note.getStartTime() < 40){
////                            System.out.println("Yes" + note.getPitch());
////                        }
//
//
//                        note.setMeasure(Integer.valueOf(measure));
//
//                        notesOfSong.add(note);
//
//                    } else if (thisnote.tagName().equals("forward")) {
//                        position = position + Integer.valueOf(thisnote.getElementsByTag("duration").text()) * divMultiplier.get(divisions);
//
//                    } else if (thisnote.tagName().equals("backup")) {
//                        //System.out.println("BACKUP" + Integer.valueOf(thisnote.getElementsByTag("duration").text()) * divMultiplier.get(divisions));
//                        position = position - Integer.valueOf(thisnote.getElementsByTag("duration").text()) * divMultiplier.get(divisions);
//
//                    }
//                }
//
//            }
//        }
//
//        return notesOfSong;
//    }
//
//
//    /**
//     * get greatest common denominator
//     *
//     * @param a
//     * @param b
//     * @return
//     */
//    private static long gcd(long a, long b) {
//        while (b > 0) {
//            long temp = b;
//            b = a % b; // % is remainder
//            a = temp;
//        }
//        return a;
//    }
//
//    private static long gcd(long[] input) {
//        long result = input[0];
//        for (int i = 1; i < input.length; i++) result = gcd(result, input[i]);
//        return result;
//    }
//
//    /**
//     * get least common multiplier
//     */
//    private static long lcm(long a, long b) {
//        return a * (b / gcd(a, b));
//    }
//
//    /**
//     * get least common multiplier of a list
//     */
//    private static Integer lcm(ArrayList<Integer> inputInt) {
//        long[] input = new long[inputInt.size()];
//
//        for (int i = 0; i < inputInt.size(); i++) {
//            input[i] = inputInt.get(i);
//
//        }
//        long result = input[0];
//        for (int i = 1; i < input.length; i++) result = lcm(result, input[i]);
//        return ((int) result);
//    }
//
//
//
//
//
//
//
//    private ArrayList<Note> setKeysOnNotes(HashMap<Integer, String> keys) {
//
//
//
//        ArrayList<Note> notesOfSong = new ArrayList<Note>();
//        Integer currentVoice = 0;
//
//        Integer duration;
//        //HashMap<Integer, Integer> positionOfVoice = new HashMap();
//        //HashMap<Integer, Integer> lastDurationsOfVoices = new HashMap();
//
//        notesOfSong.clear();
//        //positionOfVoice.clear();
//
//        Integer position = 0;
//        Integer lastDuration = 0;
//        Integer counter = 1;
//
//
////        //keep the last duration for each voice
////        for (Integer voice : voicesIndex) {
////            lastDurationsOfVoices.put(voice, 0);
////        }
////
////        //keep a start duration for each voice
////        for (Integer voice : voicesIndex) {
////            positionOfVoice.put(voice, 0);
////        }
//
//
//        for (int z = 0; z < this.doc.select("part").size(); z++ ){
////        for (Element thisPart : this.doc.select("part")) {
//
//            Integer divisions = 1;
//
//            for (int y = 0; y <  this.doc.select("part").get(z).getElementsByTag("measure").size(); y++){
////            for (Element thismeasure : this.doc.select("part").get(z).getElementsByTag("measure")) {
//
//                if (!this.doc.select("part").get(z).getElementsByTag("measure").get(y).getElementsByTag("divisions").isEmpty()) {
//                    divisions = Integer.valueOf(this.doc.select("part").get(z).getElementsByTag("measure").get(y).getElementsByTag("divisions").text());
//                }
//
//                for (int x= 0; x < this.doc.select("part").get(z).getElementsByTag("measure").get(y).children().size(); x++){
//
//                    if (this.doc.select("part").get(z).getElementsByTag("measure").get(y).children().get(x).tagName().equals("note")) {
//
////                        Note note = new Note(0);
//                        counter++;
//
//
//                        //get current voice
//                        currentVoice = Integer.valueOf(this.doc.select("part").get(z).getElementsByTag("measure").get(y).children().get(x).getElementsByTag("voice").text());
//                        //snote.setVoice(currentVoice);
//
//
//                        //get the pitch
//                        if (!this.doc.select("part").get(z).getElementsByTag("measure").get(y).children().get(x).getElementsByTag("pitch").isEmpty()) {
//                            for (Element thispitch : this.doc.select("part").get(z).getElementsByTag("measure").get(y).children().get(x).getElementsByTag("pitch")) {
//                                //                        System.out.print(thispitch.getElementsByTag("step").text());
//                                //note.setPitch(thispitch.getElementsByTag("step").text());
//
//
//                            }
//
//                        } else { //if (note.getElementsByTag("rest")!= null){
//                            //getting other elements like rests is important for determining start onsets of the next note
//                            //                    System.out.println(note.getElementsByTag("rest").text());
//
//                            //It's a rest
//                            //note.setPitch("Z");
//
//                        }
//
//                        if (this.doc.select("part").get(z).getElementsByTag("measure").get(y).children().get(x).getElementsByTag("duration").text().isEmpty()) {
//                            duration = 0;     //todo ornamentals
//                        } else {
//                            duration = Integer.valueOf(this.doc.select("part").get(z).getElementsByTag("measure").get(y).children().get(x).getElementsByTag("duration").text()) * divMultiplier.get(divisions);
//
//                        }
//
//                        //note.setDuration(duration);
//
//                        //now check if it is a chord
//                        if (!this.doc.select("part").get(z).getElementsByTag("measure").get(y).children().get(x).getElementsByTag("chord").isEmpty()) {
//
//
//
//
//                            //check if the start time = on the list.
//                            if (keys.containsKey(position)){
//                                this.doc.select("part").get(z).getElementsByTag("measure").get(y).children().get(x).append("<lyric><syllabic>single</syllabic><text>Key: "+keys.get(position)+"</text></lyric>");
//                                //System.out.println("YES" + position);
//                                keys.remove(position);
//                                //set label
//
//                            }
//
//
//                            //this.doc.select("part").get(z).getElementsByTag("measure").get(y).children().get(x).append("<lyric><syllabic>single</syllabic><text>Les</text></lyric>");
//                            //note.setStartTime(position);
//
//                            //retract previous duration
//                            //note.setStartTime(position - lastDuration);
//
//
//                        } else {
//                            //increment start time of the current voice
//                            //System.out.print(" start: " + position);
//                            //note.setStartTime(position);
//                            if (keys.containsKey(position)){
//                                this.doc.select("part").get(z).getElementsByTag("measure").get(y).children().get(x).append("<lyric><syllabic>single</syllabic><text>Key: "+keys.get(position)+"</text></lyric>");
//                                //System.out.println("YES" + position);
//                                keys.remove(position);
//                                //set label
//
//                            }
//                            //System.out.println("b");
//                            position = position + duration;
//
//
//                        }
//
//                        lastDuration = duration;
//                        //note.setCounter(counter);
//
//                        //notesOfSong.add(note);
//
//                    } else if (this.doc.select("part").get(z).getElementsByTag("measure").get(y).children().get(x).tagName().equals("forward")) {
//                        position = position + Integer.valueOf(this.doc.select("part").get(z).getElementsByTag("measure").get(y).children().get(x).getElementsByTag("duration").text()) * divMultiplier.get(divisions);
//
//                    } else if (this.doc.select("part").get(z).getElementsByTag("measure").get(y).children().get(x).tagName().equals("backup")) {
//                        //System.out.println("BACKUP" + Integer.valueOf(thisnote.getElementsByTag("duration").text()) * divMultiplier.get(divisions));
//                        position = position - Integer.valueOf(this.doc.select("part").get(z).getElementsByTag("measure").get(y).children().get(x).getElementsByTag("duration").text()) * divMultiplier.get(divisions);
//
//                    }
//                }
//
//            }
//        }
//
//        return notesOfSong;
//    }
//
//
//
//    public static ArrayList<ArrayList<Note>> organiseSlices(ArrayList<Note> inputNotes){
//
//
//        //determine the length
//        Integer length = 0;
//        for (Note thisnote : inputNotes) {
//            Integer noteEnd = thisnote.getStartTime() + thisnote.getDuration();
//            if (length < noteEnd ){
//                length = noteEnd;
//            }
//        }
//
//
//        ArrayList<ArrayList<Note>> organisedList = new ArrayList<>();
//
//        for (int i = 0; i< length; i++){
//            organisedList.add(new ArrayList<Note>());
//        }
//
//
////        for (int i = 0; i < organisedList.size(); i++){
////            organisedList.set(i, new ArrayList<>());
////        }
//
//
//        //for each note
//        for (Note thisnote : inputNotes){
//
//            //exclude rests
//            if (!thisnote.getPitch().equals("Z")){
//
//                //for each time the note lasts
//                for (int i = thisnote.getStartTime(); i < thisnote.getDuration()+ thisnote.getStartTime(); i++){
//
//                    organisedList.get(i).add(thisnote);
//
//                }
//            }
//
//
//        }
//
//
//
//
//        return organisedList;
//
//
//    }
//
//
//
//
//    //remove double notes?
//
//    public static ArrayList<ArrayList<Note>> reduceList(ArrayList<ArrayList<Note>> inputNotes) {
//
//        ArrayList<ArrayList<Note>> reduced = new ArrayList<>();
//
//        //add first element
//        reduced.add(inputNotes.get(0));
//
//        //add all next elements except when they are equal
//        for (int i = 1; i < inputNotes.size(); i++){
//
//            if (!equalLists(inputNotes.get(i-1), inputNotes.get(i))){
//                reduced.add(inputNotes.get(i));
//            }
//
//
//        }
//
//
//        return reduced;
//
//    }
//
//
//        public static boolean equalLists(ArrayList<Note> one, ArrayList<Note> two){
//        if (one == null && two == null){
//            return true;
//        }
//
//        if((one == null && two != null)
//                || one != null && two == null
//                || one.size() != two.size()){
//            return false;
//        }
//
//        //to avoid messing the order of the lists we will use a copy
//        //as noted in comments by A. R. S.
//        one = new ArrayList<Note>(one);
//        two = new ArrayList<Note>(two);
//
//        //todo sort list although normally they should be sorted already given way of importing
//        //Collections.sort(one); use comparator based on noteID
////        Collections.sort(two);
//        return one.equals(two);
//    }
//
//
//
//    public Double[][] getHorizontalInt(ArrayList<ArrayList<Note>>  song){
//        //from songmatrix
//        //compare all notes first
//
//        Double[][] transition = new Double[108][108];
//
//        for (int i = 0; i < transition.length; i++){
//            for (int j = 0; j < transition[0].length; j++){
//                transition[i][j] = 0.0;
//            }
//        }
//
//
//
//        //for each slice
//        for (int i = 0; i < song.size()-1; i++){
//
//
//            //for each note in this slice
//            for (int j = 0; j < song.get(i).size(); j++){
//                //compare each note in the next slice
//
//                for (int k = 0; k < song.get(i+1).size(); k++){
//
//                    int a = song.get(i).get(j).getMidiPitch();
//                    int b = song.get(i+1).get(k).getMidiPitch();
//                    transition[a][b]++;
//                }
//
//            }
//
//
//
//
//
//
//        }
//
//
////        Get the average
////        for each row
//        for (int i = 0; i < transition.length ; i++){
//
//
//            double sum = 0;
//            //for each column
//
//            //count total
//                for (int j = 0; j < transition[i].length; j++){
//
//                    sum = sum + transition[i][j];
//                }
//
//            for (int j = 0; j < transition[i].length; j++){
//
//                if (sum != 0) {
//                    transition[i][j] = transition[i][j] / sum;
//                }
//            }
//
//
//
//        }
//
//    return transition;
//
//
//
//    }
//
//
//
//
//
//
////get tm based on inputPitches
//    public Double[][] getHorizontalInt(ArrayList<ArrayList<Note>>  song, HashMap<Note, Integer> inputPitches) {
//        //from songmatrix
//        //compare all notes first
//
//        Double[][] transition = new Double[108][108];
//
//        for (int i = 0; i < transition.length; i++) {
//            for (int j = 0; j < transition[0].length; j++) {
//                transition[i][j] = 0.0;
//            }
//        }
//
//
//        //for each slice
//        for (int i = 0; i < song.size() - 1; i++) {
//
//
//            //for each note in this slice
//            for (int j = 0; j < song.get(i).size(); j++) {
//                //compare with each note in the next slice
//
//                for (int k = 0; k < song.get(i + 1).size(); k++) {
//
//                    //System.out.println("size: " +  song.get(i+1).size());
//
//                    int a = inputPitches.get(song.get(i).get(j));
//                    int b = inputPitches.get(song.get(i + 1).get(k));
//                    transition[a][b]++;
//                }
//
//            }
//
//
//        }
//
//
////        Get the average
////        for each row
//        for (int i = 0; i < transition.length; i++) {
//
//
//            double sum = 0;
//            //for each column
//
//            //count total
//            for (int j = 0; j < transition[i].length; j++) {
//
//                sum = sum + transition[i][j];
//            }
//
//            for (int j = 0; j < transition[i].length; j++) {
//
//                if (sum != 0) {
//                    transition[i][j] = transition[i][j] / sum;
//                }
//            }
//
//
//        }
//
//        return transition;
//    }
//
//    public void writeMatrix(Double[][] matrix, String name) throws FileNotFoundException, UnsupportedEncodingException {
//
//        PrintWriter writer = new PrintWriter(name, "UTF-8");
//
//        for (int i = 0; i < matrix.length; i++){
//            for (int j = 0; j < matrix[i].length; j++){
//
//                writer.print(matrix[i][j] + " ");
//            }
//            writer.print("\n");
//        }
//
//        writer.close();
//    }
//
//
//    public Double[][] getVerticalInt(ArrayList<ArrayList<Note>>  song) {
//        //from songmatrix
//        //compare all notes first
//
//        Double[][] transition = new Double[108][108];
//
//        for (int i = 0; i < transition.length; i++) {
//            for (int j = 0; j < transition[0].length; j++) {
//                transition[i][j] = 0.0;
//            }
//        }
//
//
//        //todo calc correct vertical!!!
//
//
//        //for each slice
//        for (int i = 0; i < song.size()-1; i++){
//
//
//            //for each note in this slice i
//            for (int j = 0; j < song.get(i).size(); j++){
//                //compare each other note in the same slice i
//
//                if (song.get(i).size() > j) {
//                    for (int k = j + 1; k < song.get(i).size(); k++) {
//
//                        //sort, smallest first
//
//                            int a = song.get(i).get(j).getMidiPitch();
//                            int b = song.get(i).get(k).getMidiPitch();
//                        if (a <= b) {
//                            transition[a][b]++;
//                        }
//                        else{
//                            transition[b][a]++;
//                        }
//                    }
//                }
//
//            }
//
//
//
//
//
//
//        }
//
//
//
//
//
////      Get the average
////      for each row
//        for (int i = 0; i < transition.length ; i++){
//
//
//            double sum = 0;
//            //for each column
//
//            //count total
//            for (int j = 0; j < transition[i].length; j++){
//
//                sum = sum + transition[i][j];
//            }
//
//            for (int j = 0; j < transition[i].length; j++){
//
//                if (sum != 0) {
//                    transition[i][j] = transition[i][j] / sum;
//                }
//            }
//
//
//
//        }
//
//        return transition;
//    }
//
//
//
//
//    public Double[][] getVerticalInt(ArrayList<ArrayList<Note>>  song, HashMap<Note, Integer> inputNotes) {
//        //from songmatrix
//        //compare all notes first
//
//        Double[][] transition = new Double[108][108];
//
//        for (int i = 0; i < transition.length; i++) {
//            for (int j = 0; j < transition[0].length; j++) {
//                transition[i][j] = 0.0;
//            }
//        }
//
//
//        //todo calc correct vertical!!!
//
//
//        //for each slice
//        for (int i = 0; i < song.size()-1; i++){
//
//
//            //for each note in this slice i
//            for (int j = 0; j < song.get(i).size(); j++){
//                //compare each other note in the same slice i
//
//                if (song.get(i).size() > j) {
//                    for (int k = j + 1; k < song.get(i).size(); k++) {
//
//                        //sort, smallest first
//
//                        int a = inputNotes.get(song.get(i).get(j));
//                        int b = inputNotes.get(song.get(i).get(k));
//                        if (a <= b) {
//                            transition[a][b]++;
//                        }
//                        else{
//                            transition[b][a]++;
//                        }
//                    }
//                }
//
//            }
//
//
//
//
//
//
//        }
//
//
//
//
//
////      Get the average
////      for each row
//        for (int i = 0; i < transition.length ; i++){
//
//
//            double sum = 0;
//            //for each column
//
//            //count total
//            for (int j = 0; j < transition[i].length; j++){
//
//                sum = sum + transition[i][j];
//            }
//
//            for (int j = 0; j < transition[i].length; j++){
//
//                if (sum != 0) {
//                    transition[i][j] = transition[i][j] / sum;
//                }
//            }
//
//
//
//        }
//
//        return transition;
//    }
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//    private void setPitchesOnNotes(HashMap<Integer, Integer> fullpitches,HashMap<Integer, Integer> spelling,
//                                   Map<Integer,
//            Note>
//            eventsInFragment) {
//
//        //get the notes in the order that they were parsed: notesOfSong
//
//
//        //todo take spelling into account
//
//            Integer currentVoice = 0;
//
//            Integer duration;
//            //HashMap<Integer, Integer> positionOfVoice = new HashMap();
//            //HashMap<Integer, Integer> lastDurationsOfVoices = new HashMap();
//
//            //notesOfSong.clear();
//            //positionOfVoice.clear();
//
//            Integer position = 0;
//            Integer lastDuration = 0;
//            Integer counter = 0;  //todo, why was this set to -1? ok it's augmented first.
//
//
////        //keep the last duration for each voice
////        for (Integer voice : voicesIndex) {
////            lastDurationsOfVoices.put(voice, 0);
////        }
////
////        //keep a start duration for each voice
////        for (Integer voice : voicesIndex) {
////            positionOfVoice.put(voice, 0);
////        }
//
//
//
//            for (int z = 0; z < this.doc.select("part").size(); z++ ){
////        for (Element thisPart : this.doc.select("part")) {
//
//                Integer divisions = 1;
//
//                //get all notes
////        for (Element thiselement : thisPart.children()) {
////        for (Element thisnote : thisPart.getElementsByTag("Note")) {
//                //go through all the elements, and only if note and forward backward
//
//                for (Element thismeasure : this.doc.select("part").get(z).getElementsByTag("measure")) {
//
//                    if (!thismeasure.getElementsByTag("divisions").isEmpty()) {
//                        divisions = Integer.valueOf(thismeasure.getElementsByTag("divisions").text());
//                    }
//
//                    for (Element thisnote : thismeasure.children()) {
////                thiselement.getElementsByTag("Note")
//
//
////                System.out.println(thisnote.tagName());
//                        if (thisnote.tagName().equals("note")) {
//
//                            //Note note = new Note(0);
//                            counter++;
//
//
//                            //System.out.println("\n note:");
//
//                            //System.out.print("DIVISIONS: " + divisions);
//
//
//                            //get current voice
//                            currentVoice = Integer.valueOf(thisnote.getElementsByTag("voice").text());
//                            //note.setVoice(currentVoice);
//
//
//                            //get the pitch
//                            if (!thisnote.getElementsByTag("pitch").isEmpty()) {
//                                for (Element thispitch : thisnote.getElementsByTag("pitch")) {
//
//                                    //if it's not a rest: replace the current pitch
//
//                                    //this.doc.select("part").get(z).getElementsByTag("measure").get(y).children().get(x).append("<lyric><syllabic>single</syllabic><text>Key: "+keys.get(position)+"</text></lyric>");
//
//
//                                    //base note of structure
//                                    if (eventsInFragment.get(counter)==null){
//
//                                        System.out.print("ok");
//                                    }
//
//                                    int midiPitch = fullpitches.get(eventsInFragment.get(counter).getId());
//
//
//                                    int spell = spelling.get(eventsInFragment.get(counter).getId());
//                                    String step = getStepFromMidi(midiPitch, spell);
//                                    int octave = getOctaveFromMidi(midiPitch);
//                                    String alter = getAlterFromMidi(midiPitch, spell);
//
//                                    thispitch.html("<step>"+step+"</step>" + alter + "<octave>"+octave+"</octave>");
//
//
//                                    //                        System.out.print(thispitch.getElementsByTag("step").text());
//                                    //note.setPitch(thispitch.getElementsByTag("step").text());
////                                    String octave = thispitch.getElementsByTag("octave").text().replaceAll("^\\s+|\\s+$|\\s*(\n)\\s*|(\\s)\\s*", "$1$2");
////                                    Integer octaveInt = Integer.parseInt(octave);
////                                    //note.setOctave(octaveInt);
////
////                                    //System.out.print("alter: " + thispitch.getElementsByTag("alter").text());
////                                    String alter = String.valueOf(thispitch.getElementsByTag("alter").text());
////                                    if (!thispitch.getElementsByTag("alter").isEmpty()) {
////
//////                                        note.setAccidentalInt(Integer.parseInt(alter));
//////
//////                                        if (alter.equals("1")) {
//////                                            note.setAccidental("#");
//////                                        } else if (alter.equals("-1")) {
//////                                            note.setAccidental("b");
//////                                        } else if (alter.equals("2")) {
//////                                            note.setAccidental("##");
//////                                        } else if (alter.equals("-2")) {
//////                                            note.setAccidental("bb");
//////                                        }
////                                    }
////                                    note.calculateMidiPitch();
//                                }
//
//                            } else { //if (note.getElementsByTag("rest")!= null){
//                                //getting other elements like rests is important for determining start onsets of the next note
//                                //                    System.out.println(note.getElementsByTag("rest").text());
//
//                                //note.setPitch("Z");
//
//
//                                //ITS A REST
//                            }
//
//
//
//                            //System.out.print("\n" + note.getPitch() + " ");
//                            //                System.out.print(" for " +  thisnote.getElementsByTag("duration").text() + ", ");
//
//                            //System.out.println(thisnote.text()+"ok"+ divisions);
//
//                            if (thisnote.getElementsByTag("duration").text().isEmpty()) {
//                                duration = 0;     //todo ornamentals
//                            } else {
//                                duration = Integer.valueOf(thisnote.getElementsByTag("duration").text()) * divMultiplier.get(divisions);
//
//                            }
//
//                            //note.setDuration(duration);
//
//                            //System.out.print(" dur: " + duration);
//                            //System.out.print("voice: " + currentVoice);
//
//                            //now check if it is a chord
//                            if (!thisnote.getElementsByTag("chord").isEmpty()) {
//
//
//                                //note.setStartTime(position);
//
//
//                                //retract previous duration
//                                //note.setStartTime(position - lastDuration);
//                                //System.out.print(" start: " + note.getStartTime());
//
//                                //System.out.print("chord");
//
//
//                            } else {
//                                //increment start time of the current voice
//                                //System.out.print(" start: " + position);
//                                //note.setStartTime(position);
//                                position = position + duration;
//
//
//                            }
//
//                            lastDuration = duration;
//                            //note.setCounter(counter);
//
//                            //notesOfSong.add(note);
//
//                        } else if (thisnote.tagName().equals("forward")) {
//                            position = position + Integer.valueOf(thisnote.getElementsByTag("duration").text()) * divMultiplier.get(divisions);
//
//                        } else if (thisnote.tagName().equals("backup")) {
//                            //System.out.println("BACKUP" + Integer.valueOf(thisnote.getElementsByTag("duration").text()) * divMultiplier.get(divisions));
//                            position = position - Integer.valueOf(thisnote.getElementsByTag("duration").text()) * divMultiplier.get(divisions);
//
//                        }
//                    }
//
//                }
//            }
//
//        }
//
//    private String getStepFromMidi(int midi, int spell) {
//
//
//        //todo check spell
//
//        String step = "";
//
//        int base =  midi % 12;
//
//        base = base - spell;
//
//        switch (base) {
//            case 1:  step = "C";
//                break;
//            case 2:  step = "D";
//                break;
//            case 3:  step = "E";
//                break;
//            case 4:  step = "E";
//                break;
//            case 5:  step = "F";
//                break;
//            case 6:  step = "F";
//                break;
//            case 7:  step = "G";
//                break;
//            case 8:  step = "G";
//                break;
//            case 9:  step = "A";
//                break;
//            case 10: step = "B";
//                break;
//            case 11: step = "B";
//                break;
//            case 0: step = "C";
//                break;
//            default: step = "Invalid";
//                break;
//        }
//
//        return step;
//    }
//
//    private String getAlterFromMidi(int midi, int spell) {
////check spell
//
//
//        String alter = "";
//
//        int base =  midi % 12;
//
//        switch (base) {
//            case 1:  alter = "<alter>1</alter>";
//                break;
//            case 3:  alter = "<alter>-1</alter>";
//                break;
//            case 6:  alter = "<alter>1</alter>";
//                break;
//            case 8:  alter = "<alter>1</alter>";
//                break;
//            case 10: alter = "<alter>-1</alter>";
//                break;
//            default: alter = "";
//                break;
//        }
//
//
//        if (spell == 1) {
//            if (alter.equals("")) {
//                alter = "<alter>1</alter>";
//            }
//            if (alter.equals("<alter>1</alter>")) {
//                alter = "<alter>2</alter>";
//            }
//            if (alter.equals("<alter>2</alter>")) {
//                alter = "<alter>3</alter>";
//            }
//            if (alter.equals("<alter>-1</alter>")) {
//                alter = "";
//            }
//            if (alter.equals("<alter>-2</alter>")) {
//                alter = "<alter>-1</alter>";
//            }
//        }
//
//            if (spell == 2) {
//                if (alter.equals("")) {
//                    alter = "<alter>2</alter>";
//                }
//                if (alter.equals("<alter>1</alter>")) {
//                    alter = "<alter>2</alter>";
//                }
//                if (alter.equals("<alter>2</alter>")) {
//                    alter = "<alter>4</alter>";
//                }
//                if (alter.equals("<alter>-1</alter>")) {
//                    alter = "<alter>1</alter>";
//                }
//                if (alter.equals("<alter>-2</alter>")) {
//                    alter = "";
//                }
//            }
//
//
//                if (spell == -1) {
//                    if (alter.equals("")) {
//                        alter = "<alter>-1</alter>";
//                    }
//                    if (alter.equals("<alter>1</alter>")) {
//                        alter = "";
//                    }
//                    if (alter.equals("<alter>2</alter>")) {
//                        alter = "<alter>1</alter>";
//                    }
//                    if (alter.equals("<alter>-1</alter>")) {
//                        alter = "<alter>-2</alter>";
//                    }
//                    if (alter.equals("<alter>-2</alter>")) {
//                        alter = "<alter>-3</alter>";
//                    }
//                }
//
//                    if (spell == -2) {
//                        if (alter.equals("")) {
//                            alter = "<alter>-2</alter>";
//                        }
//                        if (alter.equals("<alter>1</alter>")) {
//                            alter = "<alter>-1</alter>";
//                        }
//                        if (alter.equals("<alter>2</alter>")) {
//                            alter = "";
//                        }
//                        if (alter.equals("<alter>-1</alter>")) {
//                            alter = "<alter>-3</alter>";
//                        }
//                        if (alter.equals("<alter>-2</alter>")) {
//                            alter = "<alter>-4</alter>";
//                        }
//                    }
//
//
//
//
//
//        return alter;
//    }
//
//
//    private int getOctaveFromMidi(int midi) {
//
//        int octave = (int) Math.floor((midi/12 - 1)) ;
//
//        return octave;
//    }
//
//
//        public void writeXMLNewPitches(HashMap<Integer, Integer> fullpitches, HashMap<Integer, Integer> spelling,
//                                       Map<Integer,
//                Note> eventsInFragment, String filename) throws IOException {
//
//        System.out.println("Writing output file...");
//
//
//
//        setPitchesOnNotes(fullpitches, spelling, eventsInFragment);
//
//        doc.outputSettings().indentAmount(0).prettyPrint(false);  //escapeMode(Entities.EscapeMode.xhtml)
//
//
//        BufferedWriter htmlWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename), "UTF-8"));
//        htmlWriter.write(doc.toString());           //.replaceAll(">\\s+",">").replaceAll("\\s+<","<")
//
//        htmlWriter.close();
//    }
//
//
//}