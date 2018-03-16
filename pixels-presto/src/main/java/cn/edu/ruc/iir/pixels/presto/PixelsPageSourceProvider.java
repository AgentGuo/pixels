package cn.edu.ruc.iir.pixels.presto;

import cn.edu.ruc.iir.pixels.presto.impl.FSFactory;
import cn.edu.ruc.iir.pixels.presto.impl.PixelsMetadataReader;
import com.facebook.presto.spi.ColumnHandle;
import com.facebook.presto.spi.ConnectorPageSource;
import com.facebook.presto.spi.ConnectorSession;
import com.facebook.presto.spi.ConnectorSplit;
import com.facebook.presto.spi.connector.ConnectorPageSourceProvider;
import com.facebook.presto.spi.connector.ConnectorTransactionHandle;
import com.google.inject.Inject;
import io.airlift.log.Logger;

import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.requireNonNull;

/**
 * Provider Class for Pixels Page Source class.
 */
public class PixelsPageSourceProvider implements ConnectorPageSourceProvider {
    private static Logger logger = Logger.get(PixelsPageSourceProvider.class);

    private final String connectorId;
    private final PixelsMetadataReader pixelsMetadataReader;
    private final FSFactory fsFactory;

    @Inject
    public PixelsPageSourceProvider(PixelsConnectorId connectorId, PixelsMetadataReader pixelsMetadataReader, FSFactory fsFactory) {
        this.connectorId = requireNonNull(connectorId, "connectorId is null").toString();
        this.pixelsMetadataReader = requireNonNull(pixelsMetadataReader, "pixelsMetadataReader is null");
        this.fsFactory = requireNonNull(fsFactory, "fsFactory is null");
        logger.info("PixelsPageSourceProvider connectorId: " + connectorId.toString());
    }

    @Override
    public ConnectorPageSource createPageSource(ConnectorTransactionHandle transactionHandle,
                                                ConnectorSession session, ConnectorSplit split, List<ColumnHandle> columns) {
        logger.info("PixelsPageSourceProvider createPageSource: " + columns.size());
        requireNonNull(split, "split is null");
        PixelsSplit pixelsSplit = (PixelsSplit) split;
        checkArgument(pixelsSplit.getConnectorId().equals(connectorId), "connectorId is not for this connector");

        PixelsTable pixelsTable = pixelsMetadataReader.getTable(connectorId, pixelsSplit.getSchemaName(), pixelsSplit.getTableName());
        logger.info("PixelsRecordSetProvider getRecordSet columns: " + columns.size() + " " + columns.toString());

        logger.info("new PixelsRecordSet: " + pixelsSplit.getSchemaName() + ", " + pixelsSplit.getTableName() + ", " + pixelsSplit.getPath());
        return new PixelsPageSource(pixelsTable, pixelsTable.getColumns(), fsFactory, pixelsSplit.getPath(), connectorId);
    }
}
