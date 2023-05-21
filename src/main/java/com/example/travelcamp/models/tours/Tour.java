package com.example.travelcamp.models.tours;

import com.example.travelcamp.models.Order;
import com.example.travelcamp.models.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table
public class Tour {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @Column(nullable = false)
    @NotEmpty (message = "укажите название тура")
    private String title;

    @Column(nullable = false)
    @NotEmpty (message = "укажите краткое описание тура")
    @Size(min = 10, max = 200, message = "не менее 10 и не более 200 символов")
    private String descriptionShort;

    @Column
    private String descriptionFull;

    @Column
    private String duration;

    @Column
    private String distance;

    @Column
    private String elevation;

    @Column(nullable = false)
    @Min(value = 1000, message = "стоимость должна быть не менее 1000")
    @Max(value = 1000000, message = "стоимость не должна быть более 1000000")
    private double priceSmallGroup;

    @Column (nullable = false)
    @Min(value = 1000, message = "стоимость должна быть не менее 1000")
    @Max(value = 1000000, message = "стоимость не должна быть более 1000000")
    private double priceLargeGroup;

    @Column
    @Min(value = 1, message = "количество человек не может быть меньше 1")
    @Max(20)
    private int smallGroupParticipants;

    @Column
    @Min(value = 1, message = "количество человек не может быть меньше 1")
    @Max(20)
    private int largeGroupParticipants;

    private LocalDateTime dateTime;

    // --- Реализация типа туров через таблицу пока отключена: взамен используется Enum --- //
    //optional=false: значение не является обязательным
    //    @ManyToOne (optional = false)
    //    private TourType tourType;
                                        // ---  --- //

    //Реализация типа туров через Enum
    @NotEmpty (message = "укажите тип тура")
    private String tourType;

    // Храним все картинки данного тура
    //Все действия, которые выполняем с туром, повторяем для зависимых объектов.
    //Удаляем дочерний элемент, если на него не осталось ссылок
    //Lazy - при загрузке родительской сущности, дочерняя сущность загружена не будет. Вместо нее будет создан proxy-объект,
    //с помощью которого Hibernate будет отслеживать обращение к этой дочерней сущности и при первом обращении загрузит ее в память.
    @OneToMany(mappedBy = "tour", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<TourImage> tourImageList = new ArrayList<>();

    @OneToMany(mappedBy = "tours", fetch = FetchType.LAZY)
    private List<Order> orderList = new ArrayList<>();

    //Для работы с корзиной
    //@JoinTable указывает, что для реализации связи М-М создаётся промежуточная таблица product_cart (это класс Cart)
    //@JoinColumn указывает, какие колонки будут в промежуточной таблице. Первой указывается колонка, имеющая отношение к текущему классу("product_id")
    //В самой таблице tour при этом новых колонок не добавляется
    @ManyToMany()
    @JoinTable(name="tour_cart", joinColumns = @JoinColumn(name = "tour_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> userList;

    @PrePersist
    private void init() {
        dateTime = LocalDateTime.now();
    }

    public Tour(String title, String descriptionShort, String descriptionFull, String duration, String distance, String elevation, double priceSmallGroup, double priceLargeGroup, int smallGroupParticipants, int largeGroupParticipants, String tourType, List<TourImage> tourImageList, List<Order> orderList) {
        this.title = title;
        this.descriptionShort = descriptionShort;
        this.descriptionFull = descriptionFull;
        this.duration = duration;
        this.distance = distance;
        this.elevation = elevation;
        this.priceSmallGroup = priceSmallGroup;
        this.priceLargeGroup = priceLargeGroup;
        this.smallGroupParticipants = smallGroupParticipants;
        this.largeGroupParticipants = largeGroupParticipants;
        this.tourType = tourType;
        this.tourImageList = tourImageList;
        this.orderList = orderList;
    }

    public Tour() {
    }

    public void addImageToTour(TourImage image) {
    //Указываем, для какого конкретно тура предназначена привязываемая фотография
    // т.е. качестве продукта, к которому будет привязываться фотография будет текущий продукт
        image.setTour(this);
        tourImageList.add(image);
    }
    public void setImageToTour(int i, TourImage image) {
        //Указываем, для какого конкретно тура предназначена привязываемая фотография
        // т.е. качестве продукта, к которому будет привязываться фотография будет текущий продукт
        image.setTour(this);
        tourImageList.set(i, image);
    }

    public void replaceImageToProduct(int index, TourImage image,List<TourImage> tourImageList){
        //Указываем, для какого конкретно товара предназначена привязываемая фотография
        // т.е. качестве продукта, к которому будет привязываться фотография будет текущий продукт
        image.setTour(this);
        tourImageList.add(index, image); //добавляем фото в лист
        index++;
        tourImageList.remove(index);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescriptionShort() {
        return descriptionShort;
    }

    public void setDescriptionShort(String descriptionShort) {
        this.descriptionShort = descriptionShort;
    }

    public String getDescriptionFull() {
        return descriptionFull;
    }

    public void setDescriptionFull(String getDescriptionFull) {
        this.descriptionFull = getDescriptionFull;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getElevation() {
        return elevation;
    }

    public void setElevation(String elevation) {
        this.elevation = elevation;
    }

    public double getPriceSmallGroup() {
        return priceSmallGroup;
    }

    public void setPriceSmallGroup(double priceSmallGroup) {
        this.priceSmallGroup = priceSmallGroup;
    }

    public double getPriceLargeGroup() {
        return priceLargeGroup;
    }

    public void setPriceLargeGroup(double priceLargeGroup) {
        this.priceLargeGroup = priceLargeGroup;
    }

    public int getSmallGroupParticipants() {
        return smallGroupParticipants;
    }

    public void setSmallGroupParticipants(int smallGroupParticipants) {
        this.smallGroupParticipants = smallGroupParticipants;
    }

    public int getLargeGroupParticipants() {
        return largeGroupParticipants;
    }

    public void setLargeGroupParticipants(int largeGroupParticipants) {
        this.largeGroupParticipants = largeGroupParticipants;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getTourType() {
        return tourType;
    }

    public void setTourType(String tourType) {
        this.tourType = tourType;
    }

    public List<TourImage> getTourImagesList() {
        return tourImageList;
    }

    public void setTourImagesList(List<TourImage> tourImages) {
        this.tourImageList = tourImages;
    }

    public List<Order> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<Order> orders) {
        this.orderList = orders;
    }
}
