/*
	@author: Anil Doenmez
	@class: 4AHIF
	@project: NVS Projekt - Golang
*/package main

import (
	"database/sql"
	"fmt"
	//"log"
	_ "github.com/lib/pq"
	"html/template"
	"net/http"
	MQTT "github.com/eclipse/paho.mqtt.golang"
	"os"
    "os/signal"
    "syscall"
	"strings"
)

var db *sql.DB
var tpl *template.Template

var f MQTT.MessageHandler = func(client MQTT.Client, msg MQTT.Message) {
	fmt.Printf("TOPIC: %s\nMSG: %s\n", msg.Topic(), msg.Payload())
	if(msg.Payload()[0] == '{'){
		splitted := strings.Split(fmt.Sprintf("%s",msg.Payload()), "\"")
		locsplit := splitted[3] + "," + splitted[7]
		
		searchid := strings.Split(msg.Topic(), "/")[2] //Leitstelle/WE/Wels1 z.B
		//search or create
		row := db.QueryRow("SELECT * FROM cars WHERE id = $1", searchid)
		cr := Car{}
		err := row.Scan(&cr.id, &cr.location)
		switch {
		case err == sql.ErrNoRows:
			// insert values
			_, err := db.Exec("INSERT INTO cars (id, location) VALUES ($1, $2)", searchid, locsplit)
			if err != nil {
				fmt.Println("FEHLER Insert")
			}
		return
		case err != nil:
			fmt.Println("Fehler Update")
			return
		}
		_, err = db.Exec("UPDATE cars SET id = $1, location=$2 WHERE id=$1;", searchid, locsplit)
	}
}

func init() {
	var err error
	db, err = sql.Open("postgres", "postgres://postgres:postgrepw@localhost/carstore?sslmode=disable") //POSTGRES CONNECTION STRING
	if err != nil { 
		panic(err)
	}

	if err = db.Ping(); err != nil { //verifY connection to db if it is still alive
		panic(err)
	}
	fmt.Println("You connected to your database.")

	//tpl = template.Must(template.ParseGlob("templates/*.gohtml")) nicht benötigt, da keine tmpl benötigt werden
}

// export fields to templates
// fields changed to uppercase
type Car struct {
	id   string
	location  string
}

func main() {
	fmt.Printf("Angefangen1200")
	http.HandleFunc("/cars/show", carsShow)
/*	fmt.Printf("Angefangen1300")
	http.HandleFunc("/cars/create", carsCreateForm)
	fmt.Printf("Angefangen100")
	http.HandleFunc("/cars/create/process", carsCreateProcess)
	http.HandleFunc("/cars/update", carsUpdateForm)
	fmt.Printf("Angefangen1a0")
	http.HandleFunc("/cars/update/process", carsUpdateProcess)
	fmt.Printf("Angefangen1b00")
	http.HandleFunc("/cars/delete/process", carsDeleteProcess)
	fmt.Printf("Angefangen1c00")*/

	//MQTT LISTEN

	fmt.Printf("Angefangen100z")
	c := make(chan os.Signal, 1) //CHANNEL
		signal.Notify(c, os.Interrupt, syscall.SIGTERM)

        opts := MQTT.NewClientOptions().AddBroker("tcp://broker.hivemq.com:1883")
        opts.SetClientID("go-server")
            opts.SetDefaultPublishHandler(f)
        topic := "Leitstelle/#"

        opts.OnConnect = func(c MQTT.Client) {
                if token := c.Subscribe(topic, 0, f); token.Wait() && token.Error() != nil {
                        panic(token.Error())
                }
        }
        client := MQTT.NewClient(opts)
        if token := client.Connect(); token.Wait() && token.Error() != nil {
            panic(token.Error())
        } else {
                fmt.Printf("Connected to server\n")
		}
		fmt.Println("Ende")
		
	http.ListenAndServe(":8080", http.DefaultServeMux)
    <-c //send value into channel from 1 goroutine and receive those value in other goroutine
}
/*
func index(w http.ResponseWriter, r *http.Request) {
	http.Redirect(w, r, "/cars", http.StatusSeeOther)
}

func carsIndex(w http.ResponseWriter, r *http.Request) {
	if r.Method != "GET" {
		http.Error(w, http.StatusText(405), http.StatusMethodNotAllowed)
		return
	}

	rows, err := db.Query("SELECT * FROM cars")
	fmt.Println("Test select ")
	if err != nil {
		http.Error(w, http.StatusText(500), 500)
		return
	}
	defer rows.Close()
	fmt.Println("Test select 2")

	crs := make([]Car, 0)
	for rows.Next() {
		cr := Car{}
		err := rows.Scan(&cr.id, &cr.location) // order matters
		if err != nil {
			http.Error(w, http.StatusText(500), 500)
			return
		}
		crs = append(crs, cr)
	}
	if err = rows.Err(); err != nil {
		http.Error(w, http.StatusText(500), 500)
		return
	}

	fmt.Println("Test select 3")
	tpl.ExecuteTemplate(w, "cars.gohtml", crs)
	fmt.Println("Test select 31")
}*/

func carsShow(w http.ResponseWriter, r *http.Request) {
	fmt.Println("Test select 32")
	if r.Method != "GET" {
		http.Error(w, http.StatusText(405), http.StatusMethodNotAllowed)
		return
	}
	fmt.Println("Test select 4.11")
	id := r.FormValue("id")
	if id == "" {
		http.Error(w, http.StatusText(400), http.StatusBadRequest)
		return
	}
	fmt.Println("Test select 4.12")
	var idsecond string
	var locsec string
	_ = db.QueryRow("SELECT * FROM cars WHERE id = $1", id).Scan(&idsecond, &locsec)
	row := db.QueryRow("SELECT * FROM cars WHERE id = $1", id)
	fmt.Println("Test select 4.1 " + idsecond)
	cr := Car{}
	err := row.Scan(&cr.id, &cr.location)
	switch {
	case err == sql.ErrNoRows:
		http.NotFound(w, r)
		return
	case err != nil:
		http.Error(w, http.StatusText(500), http.StatusInternalServerError)
		return
	}

	fmt.Println("Test select 4")
	w.Write([]byte(locsec))
}
/*
func carsCreateForm(w http.ResponseWriter, r *http.Request) {
	tpl.ExecuteTemplate(w, "create.gohtml", nil)
}*/
/*
func carsCreateProcess(w http.ResponseWriter, r *http.Request) {
	if r.Method != "POST" {
		http.Error(w, http.StatusText(405), http.StatusMethodNotAllowed)
		return
	}

	// get form values
	cr := Car{}
	cr.id = r.FormValue("id")
	cr.location = r.FormValue("location")

	// validate form values
	if cr.id == "" || cr.location == "" {
		http.Error(w, http.StatusText(400), http.StatusBadRequest)
		return
	}

	// insert values
	_, err := db.Exec("INSERT INTO cars (id, location) VALUES ($1, $2)", cr.id, cr.location)
	if err != nil {
		http.Error(w, http.StatusText(500), http.StatusInternalServerError)
		return
	}

}

func carsUpdateForm(w http.ResponseWriter, r *http.Request) {
	if r.Method != "GET" {
		http.Error(w, http.StatusText(405), http.StatusMethodNotAllowed)
		return
	}

	id := r.FormValue("id")
	if id == "" {
		http.Error(w, http.StatusText(400), http.StatusBadRequest)
		return
	}

	row := db.QueryRow("SELECT * FROM cars WHERE id = $1", id)

	cr := Car{}
	err := row.Scan(&cr.id, &cr.location)
	switch {
	case err == sql.ErrNoRows:
		http.NotFound(w, r)
		return
	case err != nil:
		http.Error(w, http.StatusText(500), http.StatusInternalServerError)
		return
	}
}

func carsUpdateProcess(w http.ResponseWriter, r *http.Request) {
	if r.Method != "POST" {
		http.Error(w, http.StatusText(405), http.StatusMethodNotAllowed)
		return
	}

	// get form values
	cr := Car{}
	cr.id = r.FormValue("id")
	cr.location = r.FormValue("location")

	// validate form values
	if cr.id == "" || cr.location == "" {
		http.Error(w, http.StatusText(400), http.StatusBadRequest)
		return
	}

	// insert values
	_, err := db.Exec("UPDATE cars SET id = $1, location=$2 WHERE id=$1;", cr.id, cr.location)
	if err != nil {
		http.Error(w, http.StatusText(500), http.StatusInternalServerError)
		return
	}
}

func carsDeleteProcess(w http.ResponseWriter, r *http.Request) {
	if r.Method != "GET" {
		http.Error(w, http.StatusText(405), http.StatusMethodNotAllowed)
		return
	}

	id := r.FormValue("id")
	if id == "" {
		http.Error(w, http.StatusText(400), http.StatusBadRequest)
		return
	}

	// delete car
	_, err := db.Exec("DELETE FROM cars WHERE id=$1;", id)
	if err != nil {
		http.Error(w, http.StatusText(500), http.StatusInternalServerError)
		return
	}
}*/
