package nyc.c4q.foodsearch.mode.view;

import java.util.List;

/**
 * Created by yokilam on 1/12/18.
 */

public class Business {
    private String id;
    private String name;
    private String image_url;
    private boolean is_closed;
    private String url;
    private int review_count;
    private double rating;
    private Coordinates coordinates;
    private Place location;
    private String display_phone;
    private List<Categories> categories;

    public List <Categories> getCategories() {
        return categories;
    }

    public class Coordinates {
        private double latitude;
        private double longitude;

        public double getLatitude() {
            return latitude;
        }

        public double getLongitude() {
            return longitude;
        }
    }

    public class Place {
        private String address1;
        private String address2;
        private String city;
        private String zip_code;
        private String country;
        private String state;
        private List<String> display_address;

        public List <String> getDisplay_address() {
            return display_address;
        }

        public String getAddress1() {
            return address1;
        }

        public String getAddress2() {
            return address2;
        }

        public String getCity() {
            return city;
        }

        public String getZip_code() {
            return zip_code;
        }

        public String getCountry() {
            return country;
        }

        public String getState() {
            return state;
        }

    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImage_url() {
        return image_url;
    }

    public boolean isIs_closed() {
        return is_closed;
    }

    public String getUrl() {
        return url;
    }

    public int getReview_count() {
        return review_count;
    }

    public double getRating() {
        return rating;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public Place getLocation() {
        return location;
    }

    public String getDisplay_phone() {
        return display_phone;
    }

    public class Categories {
        private String alias;
        private String title;

        public String getAlias() {
            return alias;
        }

        public String getTitle() {
            return title;
        }
    }
}
