package ai.yunxi.queue;

import ai.yunxi.queue.pojo.Cat;
import ai.yunxi.queue.pojo.Dog;
import ai.yunxi.queue.pojo.Pet;
import ai.yunxi.queue.pojo.PetEnter;

import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;

/*
 * 猫狗队列
 *
 * 实现一种猫狗队列的结构，要求：
 *
 * 1.add方法-把cat或dog的实例放入队列
 *
 * 2.pollPet方法-将所有实例按照进入队列的先后顺序依次弹出
 *
 * 3.pollDog方法-将其中的dog实例按照进入队列的先后顺序依次弹出
 *
 * 4.pollCat方法-将其中的cat实例按照进入队列的先后顺序依次弹出
 *
 * 5.isEmpty方法-检查是否有dog或cat的实例
 *
 * 6.isDogEmpty方法-检查是否有dog的实例
 *
 * 7.isCatEmpty方法-检查是否有cat的实例
 *
 * 思路：
 *
 * 1.cat队列存放cat实例，dog队列存放dog实例，还有一个总队列存放全部实例
 * 存取实例需操作两个队列，pollDog或pollCat的时候总队列不能保持先进先出，需遍历总队列
 *
 * 2.必须增加一个计数项来保存pet被放入的顺序，不能改变原来的类结构，考虑新加一个保存类PetEnter
 */
public class DogCatQueue {

    private final Queue<PetEnter> dogQueue;
    private final Queue<PetEnter> catQueue;
    private long count;

    public DogCatQueue() {
        dogQueue = new LinkedList<>();
        catQueue = new LinkedList<>();
        count = 0;
    }

    public void add(Pet pet) {
        if (pet.getType().equals("dog")) {
            dogQueue.add(new PetEnter(pet, count++));
        } else if (pet.getType().equals("cat")) {
            catQueue.add(new PetEnter(pet, count++));
        } else {
            throw new RuntimeException("pet type error");
        }
    }

    public Pet pollPet() {
        if (!dogQueue.isEmpty() && !catQueue.isEmpty()) {
            long count1 = dogQueue.peek().getCount();
            assert catQueue.peek() != null;
            long count2 = catQueue.peek().getCount();
            if (count1 < count2) {
                return Objects.requireNonNull(dogQueue.poll()).getPet();
            } else {
                return Objects.requireNonNull(catQueue.poll()).getPet();
            }
        } else if (!dogQueue.isEmpty()) {
            return dogQueue.poll().getPet();
        } else if (!catQueue.isEmpty()) {
            return catQueue.poll().getPet();
        } else {
            throw new RuntimeException("queue is empty");
        }
    }

    public Dog pollDog() {
        if (!dogQueue.isEmpty()) {
            return (Dog) dogQueue.poll().getPet();
        } else {
            throw new RuntimeException("dog queue is empty");
        }
    }

    public Cat pollCat() {
        if (!catQueue.isEmpty()) {
            return (Cat) catQueue.poll().getPet();
        } else {
            throw new RuntimeException("cat queue is empty");
        }
    }

    public boolean isEmpty() {
        return dogQueue.isEmpty() && catQueue.isEmpty();
    }

    public boolean isDogEmpty() {
        return dogQueue.isEmpty();
    }

    public boolean isCatEmpty() {
        return catQueue.isEmpty();
    }

    public static void main(String[] args) {
        DogCatQueue queue = new DogCatQueue();
        queue.add(new Dog());
        queue.add(new Cat());
        System.out.println(queue.isCatEmpty());
        System.out.println(queue.isDogEmpty());
        System.out.println(queue.pollCat());
        System.out.println(queue.pollDog());
        System.out.println(queue.pollPet());
    }
}
