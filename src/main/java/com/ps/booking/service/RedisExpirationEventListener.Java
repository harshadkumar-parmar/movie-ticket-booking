public class RedisExpirationEventListener implements RedisEventListener {
    @Autowired
    private BookingRepository bookingRepository;

    @Override
    public void onExpiration(String key) {
        if (key.startsWith("booking:session:")) {
            String sessionId = key.substring("booking:session:".length());
            Booking booking = bookingRepository.findBySessionId(sessionId);
            if (booking != null) {
                booking.releaseTicketOnSessionExpiration();
                bookingRepository.save(booking);
            }
        }
    }
}