package test;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class Test {
  public static void main(String[] args) {
    List<Integer> list = new ArrayList<Integer>();// 创建列表
    for (int i = 0; i < 10; i++) {// 向列表中增加10个元素
      list.add(i);
    }
    Iterator it = list.iterator();
    System.out.print("ArrayList集合中的元素为：");
    while (it.hasNext()) {
      System.out.print(it.next() + " ");
    }
    System.out.println();
    System.out.println("逆序后为：");
    ListIterator<Integer> li = list.listIterator();// 获得ListIterator对象
    for (li = list.listIterator(); li.hasNext(); ) {// 将游标定位到列表结尾
      li.next();
    }
    for (; li.hasPrevious(); ) {// 逆序输出列表中的元素
      System.out.print(li.previous() + " ");
    }
  }
}