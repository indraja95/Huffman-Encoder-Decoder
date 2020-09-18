import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

 abstract class MaxPriorityQueue {
    public static final int BINARYHEAP = 0;
    
    public static final int CACHEFOURWAYHEAP = 1;
    public static final int PAIRINGHEAP = 2;

    public abstract void heapify(Freq_Table ft);
    public abstract void insert(HuffmanTree r);
    public abstract HuffmanTree pop();
    public abstract void printHeap();
    public abstract int getSize();
    public abstract boolean isNotEmpty();

    
}
class Freq_Table {
    private String input_file;
    public int[] freq_table;
   

    public Freq_Table(String file){
         this.input_file = file;
        this.freq_table = new int[1000000];
    }

    public Freq_Table(String file, int size){
        this.input_file = file;
        this.freq_table = new int[size];
    }

    public ArrayList<Integer> init(){
        ArrayList<Integer> res = new ArrayList<>();
        try {
            FileReader read = new FileReader(input_file);
            BufferedReader Reader = new BufferedReader(read);
            int tmp;
            String line = Reader.readLine();
            while(null!=line){
                         tmp = Integer.parseInt(line);
                         freq_table[tmp]++;
                         res.add(tmp);
                         line = Reader.readLine();
            }
            Reader.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return res;
    }

    public int get_size() { return freq_table.length; }

    public int get(int i){
        return freq_table[i];
    }
}


 class Store implements Comparable{
    private int numb;
    private int freq;
    private int huffmanCode;

    public Store(int numb, int freq) {
        this.numb = numb;
        this.freq = freq;
    }

    public Store(Store data1, Store data2) {
        this.numb = -1;
        this.freq = data1.getFreq() + data2.getFreq();
    }

     public int getNumb() {
        return numb;
    }
    
     public int getFreq() {
         return freq;
     }
    
     public void setNumb(int numb) {
         this.numb = numb;
     }

    public void setFreq(int freq) {
        this.freq = freq;
    }
    
    
   
    @Override
    public String toString() {
        return Integer.toString(numb) + " : " + Integer.toString(freq);
    }

    @Override
    public int compareTo(Object that) {
        return this.freq - ((Store) that).getFreq();
    }

    public boolean isGreaterThan(Object that) {
        return this.compareTo(that) > 0;
    }

    public boolean isLessThan(Object that) {
        return this.compareTo(that) < 0;
    }

    public boolean isGreaterThanEqualTo(Object that) {
        return this.compareTo(that) >= 0;
    }

    public boolean isLessThanEqualTo(Object that) {
        return this.compareTo(that) <= 0;
    }

    public void setHuffmanCode(int huffmanCode){
        this.huffmanCode = huffmanCode;
    }

    public int getHuffmanCode() {
        return huffmanCode;
    }
}

 class HuffmanTree implements Comparable {
    HuffmanTree lsubTree, rsubTree;
    Store data;
    private String huffmanCode;
    HuffmanTree parent;
    public int dec_data = -1;

    public HuffmanTree(){
        this.lsubTree = null;
        this.rsubTree = null;
        this.data = null;
        this.parent = null;
        this.dec_data = -1;
    }

    public HuffmanTree(int numb, int freq) {
        this.data = new Store(numb, freq);
        this.lsubTree = null;
        this.rsubTree = null;
    }

    public HuffmanTree(HuffmanTree ht1, HuffmanTree ht2) {
        if(ht1.isGreaterThan(ht2))
        {
            this.lsubTree = ht2;
            this.rsubTree = ht1;
        }
        else
        {
            this.lsubTree = ht1;
            this.rsubTree = ht2;
        }
        this.lsubTree.parent = this;
        this.rsubTree.parent = this;
        data = new Store(ht1.data, ht2.data);
    }

    public void init(Freq_Table ft, int priorityQueue){
        MaxPriorityQueue maxPriorityQueue = null;
      
        if(priorityQueue==MaxPriorityQueue.BINARYHEAP){
        	 maxPriorityQueue = new BinaryHeap();
        }
        
        else if(priorityQueue==MaxPriorityQueue.CACHEFOURWAYHEAP){
       	 maxPriorityQueue = new CacheFourWayHeap();
       }
        else  if(priorityQueue==MaxPriorityQueue.PAIRINGHEAP){
          	 maxPriorityQueue = new PairingHeap();
          }
        maxPriorityQueue.heapify(ft);
           while(maxPriorityQueue.getSize()>1){
            HuffmanTree ht1 = maxPriorityQueue.pop();
            HuffmanTree ht2 = maxPriorityQueue.pop();
            maxPriorityQueue.insert(merge(ht1,ht2));
        }

        HuffmanTree ht = maxPriorityQueue.pop();
        this.lsubTree = ht.lsubTree;
        this.rsubTree = ht.rsubTree;
        this.data = ht.data;


    }

    public HashMap<Integer, String> getSymbolTable(){
        HashMap<Integer, String> symtab = new HashMap<>();
        LinkedList<HuffmanTree> htList = new LinkedList<>();
        this.huffmanCode = "";
        htList.add(this);
        HuffmanTree ht;
        while(!htList.isEmpty()){
            ht = htList.remove(0);
            ht.lsubTree.huffmanCode = ht.huffmanCode + "0";
            ht.rsubTree.huffmanCode = ht.huffmanCode + "1";
            if(ht.lsubTree.data.getNumb() == -1){
                htList.add(ht.lsubTree);
            }
            else{
                symtab.put(ht.lsubTree.data.getNumb(),ht.lsubTree.huffmanCode);
            }
            if(ht.rsubTree.data.getNumb() == -1){
                htList.add(ht.rsubTree);
            }
            else{
                symtab.put(ht.rsubTree.data.getNumb(),ht.rsubTree.huffmanCode);
            }
        }
        return symtab;
    }

    private HuffmanTree merge(HuffmanTree ht1, HuffmanTree ht2) {
        return new HuffmanTree(ht1,ht2);
    }

    public void getHuffmanCodes(){
        int k = 0;
        if(this.hasData())
        {
        }
    }

    public boolean isGreaterThan(Object that) {
        return data.compareTo(((HuffmanTree) that).data) > 0;
    }

    public boolean isLessThan(Object that) {
        return data.compareTo(((HuffmanTree) that).data) < 0;
    }

    public boolean isGreaterThanEqualTo(Object that) {
        return data.compareTo(((HuffmanTree) that).data) >= 0;
    }

    public boolean isLessThanEqualTo(Object that) {
        return data.compareTo(((HuffmanTree) that).data) <= 0;
    }

    public boolean hasData(){
        return null != data;
    }

    @Override
    public String toString() {
        if(null!=data)
            return data.toString();
        return null;
    }

    @Override
    public int compareTo(Object that) {
        return this.data.compareTo(((HuffmanTree) that).data);
    }
}

 class BinaryHeap extends MaxPriorityQueue {

    private ArrayList<HuffmanTree> heap;

    public BinaryHeap(){
        heap = new ArrayList<>();
    }

    @Override
    public void heapify(Freq_Table ft){
        int size = ft.get_size();
        for(int i = 0;i < size; i++){
            if(ft.get(i)!=0)
                heap.add(new HuffmanTree(i,ft.get(i)));
        }
        for(int i = parentIndex(size-1); i >= 0; i--){
            bubbleDown(i);
        }
    }

    @Override
    public void insert(HuffmanTree r){
        heap.add(r);
        bubbleUp(heap.size()-1);
    }

    @Override
    public HuffmanTree pop(){
        HuffmanTree ht =peek();
        swap(0,heap.size()-1);
        heap.remove(heap.size()-1);
        bubbleDown(0);
        return ht;
    }

    public HuffmanTree peek(){
        return heap.get(0);
    }

    private void bubbleUp(int index){
        while (hasParent(index) && heap.get(parentIndex(index)).isGreaterThan(heap.get(index))) {
            swap(index, parentIndex(index));
            index = parentIndex(index);
        }
    }

    private void bubbleDown(int index){
        while (hasLeftChild(index))
        {
            if(!hasRightChild(index)){
                if(heap.get(index).isGreaterThan(heap.get(leftChildIndex(index)))){
                    swap(index, leftChildIndex(index));
                    index = leftChildIndex(index);
                }
                else{
                    return;
                }
            }
            else if (heap.get(leftChildIndex(index)).isLessThan(heap.get(rightChildIndex(index)))) {
                if(heap.get(index).isGreaterThan(heap.get(leftChildIndex(index)))){
                    swap(index, leftChildIndex(index));
                    index = leftChildIndex(index);
                }
                else{
                    return;
                }
            }
            else if(hasRightChild(index) && heap.get(rightChildIndex(index)).isLessThanEqualTo(heap.get(leftChildIndex(index)))) {
                if(heap.get(index).isGreaterThan(heap.get(rightChildIndex(index)))){
                    swap(index, rightChildIndex(index));
                    index = rightChildIndex(index);
                }
                else{
                    return;
                }
            }
            else
                return;
        }
    }

    private void swap(int i, int j) {
        HuffmanTree tmp;
        tmp = heap.get(i);
        heap.set(i,heap.get(j));
        heap.set(j,tmp);
    }

    private int leftChildIndex(int i){
        return 2*i+1;
    }

    private int rightChildIndex(int i){
        return 2*i+2;
    }

    private int parentIndex(int i){
        return (i-1)/2;
    }

    private boolean hasParent(int i){ return parentIndex(i) >= 0;    }

    private boolean hasLeftChild(int i){ return leftChildIndex(i) < heap.size();}

    private boolean hasRightChild(int i){ return rightChildIndex(i) < heap.size(); }

    @Override
    public int getSize(){
        return heap.size();
    }

    @Override
    public boolean isNotEmpty() {
        return !heap.isEmpty();
    }

    @Override
    public void printHeap() {
        System.out.println(heap);
    }
}

 class PairingHeap extends MaxPriorityQueue {
    PairingHeap firstChild;
    PairingHeap leftSibling, rightSibling;
    HuffmanTree data;
    int size;

    public PairingHeap(HuffmanTree r){
        data = r;
        size = 1;
    }

    public PairingHeap(){

    }

    @Override
    public void heapify(Freq_Table ft){
        for(int i = 0;i < ft.get_size(); i++){
            if(ft.get(i)!=0)
                this.insert(new HuffmanTree(i,ft.get(i)));
        }

    }

    @Override
    public void insert(HuffmanTree r){
        if(data == null) {
            data = r;
            size = 1;
        }
        else
            this.meld(new PairingHeap(r));
    }

    public void meld(PairingHeap other) {
        if (other.data.isLessThan(this.data)) {
            // make this the leftmost child of other
            exchangeNodes(this,other);
        }
        this.addLeftMostChild(other);
        this.size += other.getSize();
    }

    private void exchangeNodes(PairingHeap pairingHeap, PairingHeap other) {
        PairingHeap tmp = new PairingHeap();
        tmp.data = pairingHeap.data;
        tmp.leftSibling = pairingHeap.leftSibling;
        tmp.rightSibling = pairingHeap.rightSibling;
        tmp.firstChild = pairingHeap.firstChild;
        tmp.size = pairingHeap.size;
        pairingHeap.firstChild = other.firstChild;
        pairingHeap.rightSibling = other.rightSibling;
        pairingHeap.leftSibling = other.leftSibling;
        pairingHeap.data = other.data;
        pairingHeap.size = other.size;
        other.size = tmp.size;
        other.data = tmp.data;
        other.leftSibling = tmp.leftSibling;
        other.rightSibling = tmp.rightSibling;
        other.firstChild = tmp.firstChild;
    }

    @Override
    public HuffmanTree pop() {
        HuffmanTree Store = null;
        if(data != null){
            Store = data;
            if(null != firstChild) {
                LinkedList<PairingHeap> queue = new LinkedList<>();
                LinkedList<PairingHeap> queue2 = new LinkedList<>();
                PairingHeap candidate = firstChild;
                while (candidate != null) {
                    queue.addLast(candidate);
                    candidate = candidate.rightSibling;
                }
                int queueSize = queue.size();
                int i = 0;
                PairingHeap newPairingHeap;
                while (queue.size()>1) {
                    newPairingHeap = queue.remove(0);
                    newPairingHeap.meld(queue.remove(0));
                    queue.addLast(newPairingHeap);
                }
               
                this.firstChild = queue.get(0).firstChild;
                this.data = queue.get(0).data;
            }
            else{
                this.size = 0;
                this.data = null;
            }
            this.size--;
        }
        return Store;
    }

    private void addLeftMostChild(PairingHeap newChild){
        PairingHeap prevChild = this.firstChild;
        this.firstChild = newChild;
        newChild.leftSibling = this;
        newChild.rightSibling = prevChild;
        if(null!=prevChild)
            prevChild.leftSibling = newChild;
    }

    @Override
    public void printHeap() {


    }

    @Override
    public boolean isNotEmpty() {
        return data != null;
    }

    @Override
    public int getSize(){
        return this.size;
    }
}

 class CacheFourWayHeap extends MaxPriorityQueue {

    private ArrayList<HuffmanTree> heap;
  
    public CacheFourWayHeap(){
        heap = new ArrayList<>();
        for(int i = 0; i < 3; i++){
            heap.add(new HuffmanTree(1000000+i, -1));
        }
    }

    @Override
    public void heapify(Freq_Table ft){
        for(int i = 0;i < ft.get_size(); i++){
            if(ft.get(i)!=0)
                heap.add(new HuffmanTree(i,ft.get(i)));
        }
        for(int i = parentIndex(heap.size()-1); i >= 3; i--){
            bubbleDown(i);
        }
    }

    @Override
    public void insert(HuffmanTree r){
        heap.add(r);
        bubbleUp(heap.size()-1);
    }

    @Override
    public HuffmanTree pop(){
        HuffmanTree rec =peek();
        swap(3,heap.size()-1);
        heap.remove(heap.size()-1);
        bubbleDown(3);
        return rec;
    }

    public HuffmanTree peek(){
        return heap.get(3);
    }

    private void bubbleUp(int index){
        while (hasParent(index) && heap.get(parentIndex(index)).isGreaterThan(heap.get(index))) {
            swap(index, parentIndex(index));
            index = parentIndex(index);
        }
    }

    private void bubbleDown(int index){
        while (hasChild(index, 1)) {
           
            int smallestIndex = index;
            for (int i = 0; i < 4; i++)
                if (hasChild(index, i) && heap.get(childIndex(index, i)).isLessThan(heap.get(smallestIndex)))
                    smallestIndex = childIndex(index, i);
            if (smallestIndex != index) {
                swap(index, smallestIndex);
                index = smallestIndex;
            } else {
                return;
            }
        }
    }

    private void swap(int i, int j) {
        HuffmanTree tmp;
        tmp = heap.get(i);
        heap.set(i,heap.get(j));
        heap.set(j,tmp);
    }

    private int childIndex(int i, int c){
        return 4*(i-3)+c + 3;
    }

    private int parentIndex(int i){
        return (i-1)/4 + 3;
    }

    private boolean hasParent(int i){ return parentIndex(i) >= 3;    }

    private boolean hasChild(int i, int c){ return childIndex(i, c) < heap.size();}

    @Override
    public int getSize(){
        return heap.size() - 3;
    }

    @Override
    public boolean isNotEmpty() {
        return !heap.isEmpty();
    }

    @Override
    public void printHeap() {
        System.out.println(heap+"Four");
    }


}
public class encoder {

   String file ;
   
    public encoder(){

    }
    public static void main(String args[]){
    	
        String path = args[0];
              encoder e = new encoder(path);
              
             try {
                 e.encode();
               
              } catch (Exception e1) {
                  e1.printStackTrace();
              }
            }

    public encoder(String file){
        this.file = file;
        }

    public void encode() throws IOException{
        long time ;
        Freq_Table ft = new Freq_Table(file);
        ArrayList<Integer> input = ft.init();
        

        time = System.nanoTime();
      
        HuffmanTree ht2 = new HuffmanTree();
        ht2.init(ft, MaxPriorityQueue.CACHEFOURWAYHEAP);
        
        HashMap<Integer, String> symTab = ht2.getSymbolTable();
        
        System.out.println(System.nanoTime()-time+"Time for huffman") ;
        
       FileWriter fileWriter = new FileWriter("code_table.txt");
        BufferedWriter out = new BufferedWriter(fileWriter);
       Iterator<Map.Entry<Integer, String>> it = symTab.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Integer, String> pairs = it.next();
            out.write(pairs.getKey() + "\t" + pairs.getValue() + "\n");
        }
        out.close();
        

        
        StringBuilder binaryString = new StringBuilder("");
        for(Integer i:input)
            binaryString.append(symTab.get(i));
        byte[] barray = new byte[binaryString.length()/8];
        for(int i = 0; i < binaryString.length()/8; i++){
            barray[i] = (byte) Short.parseShort(binaryString.substring(8*i,8*(i+1)),2);
        }
        
        OutputStream output = null;
        output = new BufferedOutputStream(new FileOutputStream("encoded.bin"));
        output.write(barray);
        output.close();
        
    }
    
   

        
    }

