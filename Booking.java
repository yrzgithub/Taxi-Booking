/*


A --15 kms(1 hr) -- B C D E F
n =4
mini = 100 (1 st 5) and 10/km
all at 1 = A

free
nearest free
lower earning

tot_amount,id,from,to,pick_time;

 */

 import java.util.*;
 import java.lang.*;

 class Booking
 {
    static int customer_id = 0;
    public static void main(String[] args)
    {
        Scanner scan = new Scanner(System.in);
        int n = 4;
        Taxi[] taxies = new Taxi[n];

        for(int i=0;i<n;++i)
        {
            taxies[i] = new Taxi(i, 'A', 'A', 0);
        }

        while(true)
        {
            System.out.println("\nEnter your choise : \n1 Book Taxi\n2 See Taxi Details\n");
            switch(scan.nextInt())
            {
                case 1:
                System.out.println("Enter the pick up point : ");
                char pick = Character.toUpperCase(scan.next().charAt(0));

                System.out.println("Enter the destination point : ");
                char drop = Character.toUpperCase(scan.next().charAt(0));

                System.out.println("Enter the pick up time : ");
                int pick_time = scan.nextInt();

                List<Taxi> free_taxies= new ArrayList<>();
                for(Taxi taxi:taxies)
                {
                    System.out.println(String.format("Taxi %d free time : %d",taxi.id,taxi.getfreetime(pick)));
                    if(pick_time>=taxi.getfreetime(pick)) 
                    {
                        free_taxies.add(taxi);
                    }
                }

                if(free_taxies.size()==0)
                {
                    System.out.println(".......Taxi not available........");
                    continue;
                }

                int min_distance = Math.abs((free_taxies.get(0).current_place-pick)*15);
                Taxi booked_taxi = free_taxies.get(0);

                Collections.sort(free_taxies,(a,b)->b.total_amount-a.total_amount);
        
                for(Taxi t:free_taxies)
                {
                    if(min_distance>(Math.abs(t.current_place-pick)*15))
                    {
                        min_distance = Math.abs(t.current_place-pick)*15;
                        booked_taxi = t;
                    }
                }

                System.out.print("\n\n...Taxi Booked...\nTaxi No : ");
                System.out.println(booked_taxi.id+"\n\n");
                ++customer_id;
                booked_taxi.setDetails(pick, drop, pick_time);
                break;

                case 2:
                System.out.println("CUSTOMER ID     PICK UP TIME     DROP TIME     FROM     TO     TOT AMOUNT");
                Map<String,List<String>> saved = Taxi.getHistory();
                List<String> names = new ArrayList<>();
                for(int i=0;i<n;++i)
                {
                    names.add(String.format("Taxi %d",i));
                }
                for(String s:names)
                {
                    System.out.println(s);
                    List<String> save = saved.get(s);
                    if(save!=null)
                    {
                        for(String str:save)
                        {
                            System.out.println(str);
                        }
                    }
                }
                break;

                case 3:
                scan.close();
                return;

                default:
                System.out.println(".....Invalid input.....\n\n\n\n");
            }
        }

    }
 }

 class Taxi 
 {
    int id,pick_time,total_amount,drop_time,customer_id;
    char from,to,current_place;
    static Map<String,List<String>> history = new HashMap<String,List<String>>();

    Taxi(int id,char from,char to,int pick_time)
    {
        this.id = id;
        customer_id = id;
        this.from = from;
        this.to = to;
        this.current_place = to;
        this.pick_time = pick_time;
        this.drop_time = pick_time + Math.abs(from-to);
        total_amount = 0;
    }

    void setDetails(char from,char to,int pick_time)
    {
        this.from = from;
        customer_id = id;
        this.to = to;
        this.current_place = to;
        this.pick_time = pick_time;
        this.drop_time = pick_time + Math.abs(from-to);
        int diff = from-to;
        int distance = Math.abs(diff)*15;
        this.total_amount+= (distance-5)*10+100;
        SaveToHistory();
    }

    void SaveToHistory()
    {
        String save = String.format("%d           %d              %d            %c           %c            %d",customer_id,pick_time,drop_time,from,to,total_amount);
        if(save==null) return;
        List<String> current = history.get(String.format("Taxi %d",id));
        if(current==null)
        {
            current = new ArrayList<>();
        }
        current.add(save);
        history.put(String.format("Taxi %d",id),current);
    }

    static Map <String,List<String>>getHistory()
    {
        return history;
    }

    int getfreetime(char d)
    {
        return drop_time + Math.abs(current_place-d);
    }
 }