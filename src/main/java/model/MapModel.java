package model;

import java.io.*;
import java.util.ArrayList;

public class MapModel {
    private int currentChunkID;
    private ArrayList<ChunkModel> chunks;


    public MapModel(int currentChunkID, String relativePathname) {
        this.chunks = new ArrayList<>();
        this.currentChunkID = currentChunkID;
        this.readMap(relativePathname);
    }

    public ChunkModel findChunk(int id){
        for (ChunkModel chunk : this.chunks)
            if (chunk.getId() == id)
                return chunk;
        return null;
    }

    public ChunkModel thisChunk() {
        return findChunk(currentChunkID);
    }

    public void addChunk(ChunkModel chunkModel){
        this.chunks.add(chunkModel);
    }

    public void moveNorth(){
        if (findChunk(thisChunk().getNorthId()) != null) this.currentChunkID = thisChunk().getNorthId();
    }
    public void moveSouth(){
        if (findChunk(thisChunk().getSouthId()) != null) this.currentChunkID = thisChunk().getSouthId();
    }
    public void moveEast(){
        if (findChunk(thisChunk().getEastId()) != null) this.currentChunkID = thisChunk().getEastId();
    }
    public void moveWest(){
        if (findChunk(thisChunk().getWestId()) != null) this.currentChunkID = thisChunk().getWestId();
    }

    public void readMap(String relativePathname) {
        ChunkModel newChunk = new ChunkModel();
        String line = "";
        String filePath = new File(relativePathname).getAbsolutePath();
        System.out.println("Path: " + filePath);

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            int rowCounter = 0;
            while ((line = br.readLine()) != null) {
                rowCounter++;
                if (Utils.parseCSVLineIntoObject(line, rowCounter, newChunk)) {
                    this.addChunk(newChunk);
                    rowCounter = 0;
                    newChunk = new ChunkModel();
                }
            }
        } catch (IOException | NullPointerException e){
            e.printStackTrace();
        }


        // just here to test the writeMap function
        /*
        try {
            writeMap(relativePathname);
        } catch (IOException e) {
            e.printStackTrace();
        }
        */
    }


    public void writeMap(String relativePathname) throws IOException {
        ChunkModel newChunk = new ChunkModel();
        String filePath = new File(relativePathname).getAbsolutePath();
        // filePath = "/Users/joaosousa/Documents/GitHub/lpoo-2020-g64/resources/chunks2.csv";

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {

            for (int i = 0; i < this.chunks.size(); i++) {
                Utils.parseObjectIntoCSVLine(bw, this.chunks.get(i));
                if (i + 1 < this.chunks.size()) bw.write("\n");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public int getId(){ return this.currentChunkID; }
}