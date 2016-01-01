package domain;

import java.io.Serializable;

import lombok.Getter;
import lombok.ToString;
import core.Constants;
import core.Location;

@Getter
@ToString
public class SleighLocation extends Location implements Serializable {

    private static final long serialVersionUID = 1L;
    private final long id;
    private final double weight;

    public SleighLocation(long id, double weight, double latitude, double longitude) {
        super(latitude, longitude);
        this.id = id;
        this.weight = Math.abs( weight );
    }

    public static class Builder {
        
        private final long id;
        private double weight = 0;
        private double latitude;
        private double longitude;
        
        public Builder(long id) {
            this.id = id;
        }
        
        public Builder setWeight(double weight) {
            this.weight = weight;
            return this;
        }

        public Builder setLatitude(double latitude) {
            this.latitude = latitude;
            return this;
        }
        
        public Builder setLongitude(double longitude) {
            this.longitude = longitude;
            return this;
        }
        
        public SleighLocation build() {
            return new SleighLocation(id, weight, latitude, longitude);
        }

    }

    public boolean isNorthPole() {
        return this.equals(Constants.SLEIGH_NORTH_POLE);
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + (int) (id ^ (id >>> 32));
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        SleighLocation other = (SleighLocation) obj;
        if (id != other.id)
            return false;
        return true;
    }
    
}
