

public class Main  {

    public static void main(String[] args) {
        MyHashMap<String, Integer> map = new MyHashMap<>();

        map.put("apple", 10);
        map.put("banana", 20);
        map.put("cherry", 30);

        System.out.println(map.get("banana"));
        System.out.println(map.containsKeyExact("apple"));

        Integer removed = map.remove("banana");
        System.out.println("Removed: " + removed);
        System.out.println(map.get("banana"));

        map.put("apple", 99);
        System.out.println(map.get("apple"));
    }
}