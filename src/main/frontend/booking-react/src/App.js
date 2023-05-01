import React, { useState, useEffect } from "react";
import BookingForm from "./components/BookingForm";
import BookingList from "./components/BookingList";
import BlockForm from "./components/BlockForm";
import BlockList from "./components/BlockList";
import http from "./http-common";
import "./App.scss";

function App() {
  const [bookings, setBookings] = useState([]);
  const [blocks, setBlocks] = useState([]);

  // Fetch all bookings
  useEffect(() => {
    http
      .get("/bookings")
      .then((response) => setBookings(response.data))
      .catch((error) => console.error("Error fetching bookings:", error));

      http
      .get("/blocks")
      .then((response) => setBlocks(response.data))
      .catch((error) => console.error("Error fetching blocks:", error));
  }, []);

  return (
    <div className="container">
      <h1>Bookings</h1>
      <BookingForm bookings={bookings} setBookings={setBookings} blocks={blocks}/>
      <BookingList bookings={bookings} setBookings={setBookings} />
      <h1>Blocks</h1>
      <BlockForm blocks={blocks} setBlocks={setBlocks} />
      <BlockList blocks={blocks} setBlocks={setBlocks} />
    </div>
  );
}

export default App;
