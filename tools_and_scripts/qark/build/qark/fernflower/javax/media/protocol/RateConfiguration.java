package javax.media.protocol;

public interface RateConfiguration {
   RateRange getRate();

   SourceStream[] getStreams();
}
