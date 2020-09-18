import java.io.*;
import java.util.*;

class Iterator{
    byte[] Array;
    int Index = 0;
    int bitIndex = 0;
    final static char[] masks = {0x80, 0x40, 0x20, 0x10, 0x08, 0x04, 0x02, 0x01};

    public Iterator(byte[] Array){
        this.Array = Array;
    }
   
    boolean hasNext() {
        return !(bitIndex == 8 && Index == (Array.length-1));
    }

    boolean getNextBit(){
        if(bitIndex==8) {
            Index=Index+1;
            bitIndex=0;
        }
        return (Array[Index]&masks[bitIndex++]) > 0;
    }

   
}

public class decoder {
    String encoded_file ;
    String code_table ;
    public decoder(String enc_file,String code_table){
this.encoded_file=enc_file;
this.code_table=code_table;
    }

   

    public void decode() throws IOException {
       
        File encFile = new File(encoded_file);
        byte[] encoded = new byte[(int)encFile.length()];
        int totalBytesRead = 0;
        InputStream input = null;
        input = new BufferedInputStream(new FileInputStream(encFile));
        while(totalBytesRead < encoded.length){
            int bytesRemaining = encoded.length - totalBytesRead;
            int bytesRead = input.read(encoded, totalBytesRead, bytesRemaining);
            if (bytesRead > 0){
                totalBytesRead = totalBytesRead + bytesRead;
            }
        }
        input.close();
        HuffmanTree decodeTree = new HuffmanTree();
         FileReader fileReader = new FileReader(code_table);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
   
        String[] strArr;
        HuffmanTree currNode;
        String line = bufferedReader.readLine();
        while(null!=line){
            strArr = line.split("\t");
            currNode = decodeTree;
            for(int i = 0; i < strArr[1].length(); i++){
              switch(strArr[1].charAt(i)){
                    case '0':
                        if(currNode.lsubTree==null)
                            currNode.lsubTree = new HuffmanTree();
                        currNode = currNode.lsubTree;
                        break;
                    case '1':
                        if(currNode.rsubTree==null)
                            currNode.rsubTree = new HuffmanTree();
                        currNode = currNode.rsubTree;
                        break;
                    default:
                        break;
                }
            	
            	
         }
            currNode.dec_data = Integer.parseInt(strArr[0]);
            line = bufferedReader.readLine();
        }
        bufferedReader.close();
       FileWriter fWriter = new FileWriter("decoded.txt");
        BufferedWriter out = new BufferedWriter(fWriter);
       Iterator iterator = new Iterator(encoded);
        HuffmanTree traversalTree = decodeTree;
        while(iterator.hasNext()){
            if(iterator.getNextBit()) {
                traversalTree = traversalTree.rsubTree;
            }
            else {
                traversalTree = traversalTree.lsubTree;
            }
            if(-1 != traversalTree.dec_data){
                out.write(traversalTree.dec_data+"\n");
                traversalTree = decodeTree;
            }
        }

        out.close();
       
    }
    public static void main(String args[]){
    	String path = args[0];
    	String code_table = args[1];
        decoder d = new decoder(path,code_table);
        try {
         
            d.decode();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
     
}
}
