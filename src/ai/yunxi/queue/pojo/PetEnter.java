package ai.yunxi.queue.pojo;

public class PetEnter {

    private final Pet pet;
    private final long count;

    public PetEnter(Pet pet, long count) {
        this.pet = pet;
        this.count = count;
    }

    public Pet getPet() {
        return pet;
    }

    public long getCount() {
        return count;
    }
}
