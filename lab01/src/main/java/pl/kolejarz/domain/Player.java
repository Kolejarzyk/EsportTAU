package pl.kolejarz.domain;

public class Player
{
    private int id;
    private String firstName;
    private String nickName;
    private String age;
    private String team;
    private String game;


    public void setId(int id)
    {
        this.id = id;
    }

    public int getId()
    {
        return id;
    }

    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    public String getFIrstName()
    {
        return firstName;
    }


    public void setNickName(String nickName)
    {
        this.nickName = nickName;
    }

    public String getNickName()
    {
        return nickName;
    }
}