package utils;

import model.Coordinates;
import model.Movie;
import model.Person;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

public class ConvertXML {

    /**
     * Write xml representation of the object.
     *
     * @param xsw stream for writing data
     * @throws XMLStreamException
     */
    public static void convertMovieToXml(XMLStreamWriter xsw, Movie movie) throws XMLStreamException {
        xsw.writeStartElement("id");
        xsw.writeCharacters(String.valueOf(movie.getId()));
        xsw.writeEndElement();

        xsw.writeStartElement("name");
        xsw.writeCharacters(movie.getName());
        xsw.writeEndElement();

        xsw.writeStartElement("coordinates");
        convertCoordinatesToXml(xsw, movie.getCoordinates());
        xsw.writeEndElement();

        xsw.writeStartElement("creationDate");
        xsw.writeCharacters(movie.getCreationDate().toString());
        xsw.writeEndElement();

        xsw.writeStartElement("oscarsCount");
        xsw.writeCharacters(String.valueOf(movie.getOscarsCount()));
        xsw.writeEndElement();

        xsw.writeStartElement("goldenPalmCount");
        xsw.writeCharacters(String.valueOf(movie.getGoldenPalmCount()));
        xsw.writeEndElement();

        xsw.writeStartElement("length");
        xsw.writeCharacters(String.valueOf(movie.getLength()));
        xsw.writeEndElement();

        if (movie.getMpaaRating() != null) {
            xsw.writeStartElement("mpaaRating");
            xsw.writeCharacters(movie.getMpaaRating().name());
            xsw.writeEndElement();
        } else {
            xsw.writeStartElement("mpaaRating");
            xsw.writeCharacters("");
            xsw.writeEndElement();
        }

        if (movie.getScreenwriter() != null) {
            xsw.writeStartElement("screenwriter");
            convertPersonToXML(xsw, movie.getScreenwriter());
            xsw.writeEndElement();
        }
    }

    /**
     * Write xml representation of the object.
     * @param xsw stream for writing data
     * @throws XMLStreamException
     */
    public static void convertPersonToXML(XMLStreamWriter xsw, Person person) throws XMLStreamException {
        xsw.writeStartElement("name");
        xsw.writeCharacters(person.getName());
        xsw.writeEndElement();

        xsw.writeStartElement("weight");
        xsw.writeCharacters(String.valueOf(person.getWeight()));
        xsw.writeEndElement();

        xsw.writeStartElement("eyeColor");
        xsw.writeCharacters(person.getEyeColor().name());
        xsw.writeEndElement();

        xsw.writeStartElement("hairColor");
        xsw.writeCharacters(person.getHairColor().name());
        xsw.writeEndElement();

        if(person.getNationality() != null) {
            xsw.writeStartElement("nationality");
            xsw.writeCharacters(person.getNationality().name());
            xsw.writeEndElement();
        } else {
            xsw.writeStartElement("nationality");
            xsw.writeCharacters("");
            xsw.writeEndElement();
        }
    }


    /**
     * Write xml representation of the object.
     * @param xsw stream for writing data
     * @throws XMLStreamException
     */
    public static void convertCoordinatesToXml(XMLStreamWriter xsw, Coordinates coordinates) throws XMLStreamException {
        xsw.writeStartElement("x");
        xsw.writeCharacters(String.valueOf(coordinates.getX()));
        xsw.writeEndElement();

        xsw.writeStartElement("y");
        xsw.writeCharacters(coordinates.getX().toString());
        xsw.writeEndElement();
    }
}
